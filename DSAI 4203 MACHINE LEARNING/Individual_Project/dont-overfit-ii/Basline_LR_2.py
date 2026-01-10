import pandas as pd
import numpy as np
from sklearn.linear_model import LogisticRegression
from sklearn.preprocessing import StandardScaler
from sklearn.feature_selection import SelectKBest, f_classif

# FIX: Load correct files - train.csv and test.csv
train = pd.read_csv('C:/Users/Zhu Jin Shun/Desktop/DSAI 4203 MACHINE LEARNING/Individual_Project/dont-overfit-ii/train.csv')
test = pd.read_csv('C:/Users/Zhu Jin Shun/Desktop/DSAI 4203 MACHINE LEARNING/Individual_Project/dont-overfit-ii/test.csv')

print(f"Train shape: {train.shape}")
print(f"Test shape: {test.shape}")

print("Train columns:", train.columns.tolist())
print("Test columns:", test.columns.tolist())

# FIX: Better target column identification with error handling
target_cols = [col for col in train.columns if col not in test.columns and col != 'id']
if target_cols:
    target_col = target_cols[0]
else:
    # If no target column found, use manual inspection
    print("WARNING: No obvious target column found. Checking data structure...")
    print("First few rows of train:")
    print(train.head())
    print("First few rows of test:")
    print(test.head())
    
    # For this competition, target should be the last column in train
    target_col = train.columns[-1]
    print(f"Using last column '{target_col}' as target")

print(f"Using '{target_col}' as target column")

print(f"Target unique values: {sorted(train[target_col].unique())}")
print(f"Target value counts:\n{train[target_col].value_counts()}")

# Ensure target is binary (0,1)
if set(train[target_col].unique()) != {0, 1}:
    print("Warning: Target is not binary 0/1. Converting to binary...")
    train[target_col] = (train[target_col] > train[target_col].median()).astype(int)
    print(f"Converted target value counts:\n{train[target_col].value_counts()}")

# Prepare features - IMPROVED: Use all numeric features except id
feature_cols = [col for col in train.columns if col not in ['id', target_col] and pd.api.types.is_numeric_dtype(train[col])]

print(f"Using {len(feature_cols)} features")

X_train = train[feature_cols]
y_train = train[target_col].astype(int)
X_test = test[feature_cols]

print(f"X_train shape: {X_train.shape}, y_train shape: {y_train.shape}")
print(f"X_test shape: {X_test.shape}")

k_features = min(30, len(feature_cols)) 
print(f"Selecting top {k_features} features...")

selector = SelectKBest(f_classif, k=k_features)
X_train_selected = selector.fit_transform(X_train, y_train)
X_test_selected = selector.transform(X_test)

# Scale the features
scaler = StandardScaler()
X_train_scaled = scaler.fit_transform(X_train_selected)
X_test_scaled = scaler.transform(X_test_selected)

model = LogisticRegression(
    C=0.1,
    penalty='l1', 
    random_state=42,
    solver='liblinear',
    max_iter=1000
)

print("Training model...")
model.fit(X_train_scaled, y_train)

# Check training accuracy
train_accuracy = model.score(X_train_scaled, y_train)
print(f"Training accuracy: {train_accuracy:.4f}")

print("Making predictions...")
test_pred = model.predict_proba(X_test_scaled)[:, 1]

# Create submission file
submission = pd.DataFrame({
    'id': test['id'],
    'target': test_pred
})

print(f"Submission shape: {submission.shape}")

# FIX: Ensure we have exactly 19750 rows
expected_rows = 19750
if len(submission) != expected_rows:
    print(f"WARNING: Expected {expected_rows} rows, but got {len(submission)}")
    # If missing rows, we need to investigate
else:
    print(f"✓ Submission has correct number of rows: {len(submission)}")

# Verify submission format
print("\nSubmission sample:")
print(submission.head())
print(f"\nTarget value range: [{submission['target'].min():.4f}, {submission['target'].max():.4f}]")

# Save submission
submission.to_csv('submission3.csv', index=False)
print("Submission saved as 'submission3.csv'")

# Additional validation
print(f"\nPrediction statistics:")
print(f"Mean prediction: {submission['target'].mean():.4f}")
print(f"Std prediction: {submission['target'].std():.4f}")
print(f"Predictions > 0.5: {(submission['target'] > 0.5).sum()}")
print(f"Predictions < 0.5: {(submission['target'] < 0.5).sum()}")