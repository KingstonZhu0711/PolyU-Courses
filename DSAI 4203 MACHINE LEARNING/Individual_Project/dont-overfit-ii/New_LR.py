import pandas as pd
import numpy as np
from sklearn.linear_model import LogisticRegression
from sklearn.preprocessing import StandardScaler
from sklearn.model_selection import cross_val_score, StratifiedKFold, GridSearchCV
from sklearn.feature_selection import SelectKBest, f_classif, RFE
from sklearn.metrics import accuracy_score, roc_auc_score
from sklearn.pipeline import Pipeline
import warnings
warnings.filterwarnings('ignore')

def load_and_prepare_data():
    """Load and prepare the dataset"""
    train = pd.read_csv('C:/Users/Zhu Jin Shun/Desktop/DSAI 4203 MACHINE LEARNING/Individual_Project/dont-overfit-ii/train.csv')
    test = pd.read_csv('C:/Users/Zhu Jin Shun/Desktop/DSAI 4203 MACHINE LEARNING/Individual_Project/dont-overfit-ii/test.csv')
    
    print(f"Original data shapes:")
    print(f"Train: {train.shape}, Test: {test.shape}")
    print(f"Target distribution:\n{train['target'].value_counts()}")
    
    return train, test

def create_robust_pipeline():
    """Create a robust pipeline with feature selection and regularized logistic regression"""
    pipeline = Pipeline([
        ('scaler', StandardScaler()),
        ('feature_selector', SelectKBest(f_classif, k=50)),  # Reduce features to combat overfitting
        ('classifier', LogisticRegression(
            random_state=42,
            max_iter=1000,
            class_weight='balanced'
        ))
    ])
    return pipeline

def cross_validate_model(X, y, pipeline, cv_strategy=5):
    """Perform cross-validation to estimate real performance"""
    cv_scores = cross_val_score(pipeline, X, y, cv=StratifiedKFold(n_splits=cv_strategy, shuffle=True, random_state=42), 
                               scoring='roc_auc')
    return cv_scores

def tune_hyperparameters(X, y):
    """Tune hyperparameters using cross-validation"""
    print("Tuning hyperparameters...")
    
    # Define parameter grid
    param_grid = {
        'feature_selector__k': [30, 50, 80, 100],
        'classifier__C': [0.001, 0.01, 0.1, 1, 10],  # Regularization strength
        'classifier__penalty': ['l1', 'l2'],
        'classifier__solver': ['liblinear']
    }
    
    pipeline = create_robust_pipeline()
    
    # Use fewer CV folds for speed since dataset is small
    grid_search = GridSearchCV(
        pipeline, param_grid, 
        cv=StratifiedKFold(n_splits=3, shuffle=True, random_state=42),
        scoring='roc_auc',
        n_jobs=-1
    )
    
    grid_search.fit(X, y)
    
    print(f"Best parameters: {grid_search.best_params_}")
    print(f"Best cross-validation score: {grid_search.best_score_:.4f}")
    
    return grid_search.best_estimator_

def evaluate_model(model, X, y):
    """Comprehensive model evaluation"""
    # Cross-validation scores
    cv_scores = cross_validate_model(X, y, model, cv_strategy=5)
    
    print(f"\n=== Model Evaluation ===")
    print(f"Cross-validation AUC scores: {cv_scores}")
    print(f"Mean CV AUC: {cv_scores.mean():.4f} (+/- {cv_scores.std() * 2:.4f})")
    
    return cv_scores.mean()

def main():
    print("=== Robust Logistic Regression for Dont-Overfit-II ===")
    print("Focus: Reducing variance and improving generalization\n")
    
    # Load data
    train, test = load_and_prepare_data()
    
    # Prepare features and target
    X_train = train.drop(columns=['id', 'target'])
    y_train = train['target']
    X_test = test.drop(columns=['id'])
    test_ids = test['id']
    
    print(f"\nTraining features: {X_train.shape}")
    
    # Strategy 1: Simple regularized model with feature selection
    print("\n" + "="*60)
    print("STRATEGY 1: Simple Regularized Model")
    print("="*60)
    
    simple_pipeline = create_robust_pipeline()
    simple_score = evaluate_model(simple_pipeline, X_train, y_train)
    
    # Strategy 2: Hyperparameter tuning
    print("\n" + "="*60)
    print("STRATEGY 2: Hyperparameter Tuning")
    print("="*60)
    
    best_model = tune_hyperparameters(X_train, y_train)
    
    # Strategy 3: Ensemble of different feature sets
    print("\n" + "="*60)
    print("STRATEGY 3: Ensemble Approach")
    print("="*60)
    
    # Train multiple models with different feature sets
    feature_sets = [30, 50, 80]
    models = []
    predictions = []
    
    for k in feature_sets:
        print(f"\nTraining model with {k} features...")
        model = Pipeline([
            ('scaler', StandardScaler()),
            ('feature_selector', SelectKBest(f_classif, k=k)),
            ('classifier', LogisticRegression(
                C=0.1,  # Strong regularization
                penalty='l2',
                random_state=42,
                max_iter=1000,
                class_weight='balanced'
            ))
        ])
        
        model.fit(X_train, y_train)
        models.append(model)
        
        # Cross-validation score
        cv_score = cross_validate_model(X_train, y_train, model, cv_strategy=3)
        print(f"CV AUC for k={k}: {cv_score.mean():.4f}")
        
        # Predict on test set
        pred = model.predict_proba(X_test)[:, 1]
        predictions.append(pred)
    
    # Ensemble predictions (average)
    ensemble_predictions = np.mean(predictions, axis=0)
    
    # Compare with best tuned model
    print(f"\nComparing approaches:")
    print(f"Simple model CV AUC: {simple_score:.4f}")
    print(f"Tuned model CV AUC: {evaluate_model(best_model, X_train, y_train):.4f}")
    
    # Final model selection - use the ensemble approach
    print("\n" + "="*60)
    print("FINAL MODEL: Using Ensemble Approach")
    print("="*60)
    
    # Create final submission using ensemble
    final_predictions = ensemble_predictions
    
    # Create submission file
    submission = pd.DataFrame({
        'id': test_ids,
        'target': final_predictions
    })
    
    # Save submission
    submission_file = 'submission4.csv'
    submission.to_csv(submission_file, index=False)
    
    print(f"\n=== Submission Created ===")
    print(f"File saved: {submission_file}")
    print(f"Predictions range: [{final_predictions.min():.4f}, {final_predictions.max():.4f}]")
    print(f"Mean prediction: {final_predictions.mean():.4f}")
    
    # Additional analysis
    print(f"\n=== Key Improvements Applied ===")
    print("✓ Strong regularization to reduce variance")
    print("✓ Feature selection to combat overfitting")
    print("✓ Cross-validation for reliable performance estimation")
    print("✓ Ensemble method to reduce variance")
    print("✓ Hyperparameter tuning for optimal performance")
    print("✓ Class weighting for imbalanced data")

if __name__ == "__main__":
    main()