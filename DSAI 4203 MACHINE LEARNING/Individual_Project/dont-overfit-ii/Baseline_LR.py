import pandas as pd
import numpy as np
from sklearn.linear_model import LogisticRegression
from sklearn.preprocessing import StandardScaler

train = pd.read_csv('C:/Users/Zhu Jin Shun/Desktop/DSAI 4203 MACHINE LEARNING/Individual_Project/dont-overfit-ii/train.csv')
test = pd.read_csv('C:/Users/Zhu Jin Shun/Desktop/DSAI 4203 MACHINE LEARNING/Individual_Project/dont-overfit-ii/test.csv')

print(f"Train shape: {train.shape}")
print(f"Test shape: {test.shape}")

print("Train columns:", train.columns.tolist())
print("Test columns:", test.columns.tolist())

target_cols = [col for col in train.columns if col not in test.columns and col != 'id']
if target_cols:
    target_col = target_cols[0]
else:
    target_col = train.columns[-1]

print(f"Using '{target_col}' as target column")

print(f"Target unique values: {sorted(train[target_col].unique())}")
print(f"Target value counts:\n{train[target_col].value_counts()}")

if set(train[target_col].unique()) != {0, 1}:
    print("Warning: Target is not binary 0/1. Converting to binary...")
    train[target_col] = (train[target_col] > train[target_col].median()).astype(int)
    print(f"Converted target value counts:\n{train[target_col].value_counts()}")

# Prepare features - use only numeric columns that exist in both datasets
feature_cols = []
for col in train.columns:
    if (col not in ['id', target_col] and 
        col in test.columns and 
        pd.api.types.is_numeric_dtype(train[col])):
        feature_cols.append(col)

print(f"Using {len(feature_cols)} features")

X_train = train[feature_cols]
y_train = train[target_col].astype(int)  # Ensure integer type
X_test = test[feature_cols]

print(f"X_train shape: {X_train.shape}, y_train shape: {y_train.shape}")
print(f"X_test shape: {X_test.shape}")

# Scale the features
scaler = StandardScaler()
X_train_scaled = scaler.fit_transform(X_train)
X_test_scaled = scaler.transform(X_test)

model = LogisticRegression(
    C=0.1,
    penalty='l2',
    random_state=42,
    solver='liblinear'
)

print("Training model...")
model.fit(X_train_scaled, y_train)

print("Making predictions...")
test_pred = model.predict_proba(X_test_scaled)[:, 1]

# Create submission file
submission = pd.DataFrame({
    'id': test['id'],
    'target': test_pred
})

print(f"Submission shape: {submission.shape}")

# Verify submission format
print("\nSubmission sample:")
print(submission.head())
print(f"\nTarget value range: [{submission['target'].min():.4f}, {submission['target'].max():.4f}]")

# Save submission
submission.to_csv('submission.csv', index=False)
print("Submission saved as 'submission.csv'")