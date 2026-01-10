import pandas as pd
import numpy as np
from sklearn.linear_model import LogisticRegression
from sklearn.preprocessing import StandardScaler
from sklearn.feature_selection import SelectKBest, f_classif
from sklearn.utils import resample

# ----------------------
# Data Preparation Functions
# ----------------------
def create_balanced_dataset(train_data, target_col, samples_per_class=160):
    """Create balanced dataset with 160 samples per class"""
    X = train_data.drop(columns=['id', target_col])
    y = train_data[target_col]
    df = pd.concat([X, y], axis=1)
    
    df_class_0 = df[df[target_col] == 0]
    df_class_1 = df[df[target_col] == 1]
    
    df_class_0_balanced = resample(df_class_0, replace=True, n_samples=samples_per_class, random_state=42)
    df_class_1_balanced = resample(df_class_1, replace=True, n_samples=samples_per_class, random_state=42)
    
    df_balanced = pd.concat([df_class_0_balanced, df_class_1_balanced])
    return df_balanced.drop(columns=[target_col]), df_balanced[target_col]

def prepare_data(train, test, target_col):
    """Prepare balanced, selected, and normalized features"""
    # Step 1: Create balanced training data (160 samples per class)
    X_balanced, y_balanced = create_balanced_dataset(train, target_col)
    print(f"Balanced data shape: {X_balanced.shape}")

    # Step 2: Feature selection (on raw balanced data, before scaling)
    k_features = min(30, X_balanced.shape[1])
    print(f"Selecting top {k_features} features...")
    selector = SelectKBest(f_classif, k=k_features)
    X_train_selected = selector.fit_transform(X_balanced, y_balanced)  # Numpy array
    selected_mask = selector.get_support()  # Boolean mask for selected features
    selected_features = X_balanced.columns[selected_mask].tolist()  # Names of selected features

    # Step 3: Normalize ONLY the selected features
    scaler = StandardScaler()
    X_train_scaled = scaler.fit_transform(X_train_selected)  # Scale selected features

    # Prepare test data: use same selected features and scaler
    X_test = test[X_balanced.columns]  # Ensure test has same features as balanced train
    X_test_selected = X_test[selected_features]  # Keep only selected features
    X_test_scaled = scaler.transform(X_test_selected)  # Scale with training scaler

    return {
        'X_train': X_train_scaled,
        'y_train': y_balanced.astype(int),
        'X_test': X_test_scaled,
        'test_ids': test['id'],
        'selected_features': selected_features
    }

# ----------------------
# Main Execution
# ----------------------
if __name__ == "__main__":
    # Load data
    train = pd.read_csv('C:/Users/Zhu Jin Shun/Desktop/DSAI 4203 MACHINE LEARNING/Individual_Project/dont-overfit-ii/train.csv')
    test = pd.read_csv('C:/Users/Zhu Jin Shun/Desktop/DSAI 4203 MACHINE LEARNING/Individual_Project/dont-overfit-ii/test.csv')

    # Identify target column
    target_cols = [col for col in train.columns if col not in test.columns and col != 'id']
    target_col = target_cols[0] if target_cols else train.columns[-1]
    print(f"Using '{target_col}' as target column")

    # Ensure target is binary
    if set(train[target_col].unique()) != {0, 1}:
        train[target_col] = (train[target_col] > train[target_col].median()).astype(int)

    # Prepare data (balanced, selected, scaled)
    data = prepare_data(train, test, target_col)

    # Train logistic regression
    model = LogisticRegression(
        C=0.1, penalty='l1', solver='liblinear', random_state=42, max_iter=1000
    )
    model.fit(data['X_train'], data['y_train'])
    print(f"Training accuracy: {model.score(data['X_train'], data['y_train']):.4f}")

    # Generate predictions
    test_pred = model.predict_proba(data['X_test'])[:, 1]

    # Create submission
    submission = pd.DataFrame({
        'id': data['test_ids'],
        'target': test_pred
    })

    # Validate and save
    print(f"Submission shape: {submission.shape}")
    submission.to_csv('submission3.csv', index=False)
    print("Submission saved as 'submission3.csv'")