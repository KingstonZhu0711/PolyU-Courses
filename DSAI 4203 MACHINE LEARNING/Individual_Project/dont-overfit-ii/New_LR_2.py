import pandas as pd
import numpy as np
from sklearn.linear_model import LogisticRegression
from sklearn.preprocessing import RobustScaler, StandardScaler
from sklearn.model_selection import cross_val_score, StratifiedKFold, GridSearchCV
from sklearn.feature_selection import RFE, SelectKBest, f_classif
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

def scale_features(df, method='robust'):
    """Scale features using RobustScaler (more outlier-resistant)"""
    if method == 'robust':
        scaler = RobustScaler()
    else:
        scaler = StandardScaler()
    
    scaled_df = pd.DataFrame(scaler.fit_transform(df), columns=df.columns)
    print(f"Features scaled using {method} (median=0, IQR=1)")
    return scaled_df

def tune_logistic_regression(X, y):
    """Tune logistic regression with comprehensive parameter search"""
    print("Tuning Logistic Regression...")
    
    param_grid = {
        "C": [0.001, 0.01, 0.1, 1, 10],
        "penalty": ['l1', 'l2'],
        "solver": ['liblinear', 'saga'],
        "tol": [1e-4, 1e-3],
        "class_weight": ['balanced', None]
    }
    
    model = LogisticRegression(random_state=42, max_iter=2000)
    grid_search = GridSearchCV(model, param_grid, cv=5, scoring='roc_auc', n_jobs=-1)
    grid_search.fit(X, y)
    
    print(f"Best Parameters: {grid_search.best_params_}")
    print(f"Best CV Score: {grid_search.best_score_:.4f}")
    
    return grid_search.best_estimator_, grid_search.best_score_

def apply_rfe_feature_selection(model, X, y, n_features=10):
    """Apply Recursive Feature Elimination for aggressive feature selection"""
    print(f"Applying RFE to select {n_features} best features...")
    
    selector = RFE(model, n_features_to_select=n_features, step=1)
    selector.fit(X, y)
    
    # Get selected features
    selected_features = X.columns[selector.support_]
    X_selected = X[selected_features]
    
    print(f"Selected {len(selected_features)} features")
    print(f"Selected features: {list(selected_features)}")
    
    return X_selected, selector

def evaluate_model_performance(model, X, y, model_name="Model"):
    """Comprehensive model evaluation with multiple metrics"""
    # Cross-validation scores
    cv_scores = cross_val_score(model, X, y, 
                               cv=StratifiedKFold(n_splits=5, shuffle=True, random_state=42), 
                               scoring='roc_auc')
    
    # Training performance
    if hasattr(model, 'predict_proba'):
        train_preds = model.predict_proba(X)[:, 1]
    else:
        train_preds = model.predict(X)
    
    train_auc = roc_auc_score(y, train_preds)
    
    print(f"\n{model_name} Performance:")
    print(f"Training ROC AUC: {train_auc:.4f}")
    print(f"CV ROC AUC: {cv_scores.mean():.4f} (+/- {cv_scores.std() * 2:.4f})")
    print(f"CV Scores: {[f'{score:.4f}' for score in cv_scores]}")
    
    return train_auc, cv_scores.mean(), cv_scores

def create_feature_selection_summary(models, performances, X_train_scaled):
    """Create a comprehensive summary table of feature selection results"""
    print("\n" + "="*60)
    print("FEATURE SELECTION AND MODEL PERFORMANCE SUMMARY")
    print("="*60)
    
    summary_data = []
    
    for i, ((model_name, model, selector), cv_auc) in enumerate(zip(models, performances)):
        # Get RFE rankings for all features
        feature_rankings = selector.ranking_
        feature_names = X_train_scaled.columns
        
        # Get selected features (ranking = 1)
        selected_mask = selector.support_
        selected_features = feature_names[selected_mask]
        selected_rankings = feature_rankings[selected_mask]
        
        # Create detailed feature information
        for feature, ranking in zip(selected_features, selected_rankings):
            summary_data.append({
                'Model': model_name,
                'Model_Index': i + 1,
                'CV_AUC': cv_auc,
                'Feature': feature,
                'RFE_Ranking': ranking,
                'Num_Features_Selected': len(selected_features)
            })
    
    # Create summary DataFrame
    summary_df = pd.DataFrame(summary_data)
    
    # Display the summary table
    print("\nDetailed Feature Selection Summary:")
    print("-" * 80)
    
    # Group by model for better readability
    for model_name in summary_df['Model'].unique():
        model_data = summary_df[summary_df['Model'] == model_name]
        cv_auc = model_data['CV_AUC'].iloc[0]
        num_features = model_data['Num_Features_Selected'].iloc[0]
        
        print(f"\n{model_name}:")
        print(f"  CV AUC: {cv_auc:.4f}, Features Selected: {num_features}")
        print(f"  Selected Features: {list(model_data['Feature'])}")
    
    # Create a pivot table for feature frequency across models
    print("\n" + "="*60)
    print("FEATURE FREQUENCY ACROSS MODELS")
    print("="*60)
    
    feature_frequency = summary_df.groupby('Feature').agg({
        'Model': 'count',
        'CV_AUC': 'mean'
    }).rename(columns={'Model': 'Times_Selected', 'CV_AUC': 'Avg_CV_AUC'})
    
    feature_frequency = feature_frequency.sort_values('Times_Selected', ascending=False)
    print(f"\nFeatures selected across {len(models)} models:")
    print(feature_frequency.head(15))  # Show top 15 most frequently selected features
    
    return summary_df, feature_frequency

def create_diverse_models(X_train, y_train, X_test):
    """Create multiple diverse models with different strategies"""
    models = []
    predictions = []
    performances = []
    
    print("\nCreating diverse model ensemble...")
    
    # Strategy 1: Strong L1 Regularization with RFE
    print("\n1. Strong L1 Regularization (10 features)")
    lr_l1 = LogisticRegression(
        C=0.1, penalty='l1', solver='liblinear', 
        class_weight='balanced', random_state=42, max_iter=2000
    )
    X_l1_selected, l1_selector = apply_rfe_feature_selection(lr_l1, X_train, y_train, n_features=10)
    lr_l1.fit(X_l1_selected, y_train)
    X_test_l1 = X_test[X_l1_selected.columns]
    l1_preds = lr_l1.predict_proba(X_test_l1)[:, 1]
    
    l1_train_auc, l1_cv_auc, l1_cv_scores = evaluate_model_performance(
        lr_l1, X_l1_selected, y_train, "L1 Regularized Model"
    )
    
    models.append(('l1_rfe_10', lr_l1, l1_selector))
    predictions.append(l1_preds)
    performances.append(l1_cv_auc)
    
    # Strategy 2: Moderate L2 Regularization with more features
    print("\n2. Moderate L2 Regularization (20 features)")
    lr_l2 = LogisticRegression(
        C=1, penalty='l2', solver='liblinear',
        class_weight='balanced', random_state=43, max_iter=2000
    )
    X_l2_selected, l2_selector = apply_rfe_feature_selection(lr_l2, X_train, y_train, n_features=20)
    lr_l2.fit(X_l2_selected, y_train)
    X_test_l2 = X_test[X_l2_selected.columns]
    l2_preds = lr_l2.predict_proba(X_test_l2)[:, 1]
    
    l2_train_auc, l2_cv_auc, l2_cv_scores = evaluate_model_performance(
        lr_l2, X_l2_selected, y_train, "L2 Regularized Model"
    )
    
    models.append(('l2_rfe_20', lr_l2, l2_selector))
    predictions.append(l2_preds)
    performances.append(l2_cv_auc)
    
    # Strategy 3: Very Strong Regularization with few features
    print("\n3. Very Strong Regularization (5 features)")
    lr_strong = LogisticRegression(
        C=0.01, penalty='l1', solver='liblinear',
        class_weight='balanced', random_state=44, max_iter=2000
    )
    X_strong_selected, strong_selector = apply_rfe_feature_selection(lr_strong, X_train, y_train, n_features=5)
    lr_strong.fit(X_strong_selected, y_train)
    X_test_strong = X_test[X_strong_selected.columns]
    strong_preds = lr_strong.predict_proba(X_test_strong)[:, 1]
    
    strong_train_auc, strong_cv_auc, strong_cv_scores = evaluate_model_performance(
        lr_strong, X_strong_selected, y_train, "Strong Regularized Model"
    )
    
    models.append(('strong_rfe_5', lr_strong, strong_selector))
    predictions.append(strong_preds)
    performances.append(strong_cv_auc)
    
    # Strategy 4: Balanced model with feature selection
    print("\n4. Balanced Model (15 features)")
    lr_balanced = LogisticRegression(
        C=0.5, penalty='l2', solver='saga',
        class_weight='balanced', random_state=45, max_iter=2000
    )
    X_balanced_selected, balanced_selector = apply_rfe_feature_selection(lr_balanced, X_train, y_train, n_features=15)
    lr_balanced.fit(X_balanced_selected, y_train)
    X_test_balanced = X_test[X_balanced_selected.columns]
    balanced_preds = lr_balanced.predict_proba(X_test_balanced)[:, 1]
    
    balanced_train_auc, balanced_cv_auc, balanced_cv_scores = evaluate_model_performance(
        lr_balanced, X_balanced_selected, y_train, "Balanced Model"
    )
    
    models.append(('balanced_rfe_15', lr_balanced, balanced_selector))
    predictions.append(balanced_preds)
    performances.append(balanced_cv_auc)
    
    return models, predictions, performances

def create_weighted_ensemble(predictions, performances):
    """Create weighted ensemble based on model performances"""
    # Normalize performances to get weights
    performances_array = np.array(performances)
    weights = performances_array / performances_array.sum()
    
    print(f"\nModel Weights for Ensemble:")
    for i, (weight, perf) in enumerate(zip(weights, performances)):
        print(f"  Model {i+1}: weight = {weight:.3f}, CV AUC = {perf:.4f}")
    
    # Weighted average of predictions
    ensemble_preds = np.average(predictions, axis=0, weights=weights)
    
    return ensemble_preds, weights

def main():
    print("=== Multi-Model Regularized Ensemble for Dont-Overfit-II ===")
    print("Strategy: Robust Scaling + Multiple RFE Models + Weighted Ensemble\n")
    
    # Load data
    train, test = load_and_prepare_data()
    
    # Prepare features and target
    X_train_raw = train.drop(columns=['id', 'target'])
    y_train = train['target']
    X_test_raw = test.drop(columns=['id'])
    test_ids = test['id']
    
    print(f"\nTraining features: {X_train_raw.shape}")
    
    # Step 1: Robust Scaling
    print("\n" + "="*60)
    print("STEP 1: Robust Feature Scaling")
    print("="*60)
    
    X_train_scaled = scale_features(X_train_raw, 'robust')
    X_test_scaled = scale_features(X_test_raw, 'robust')
    
    # Step 2: Hyperparameter Tuning for Baseline
    print("\n" + "="*60)
    print("STEP 2: Baseline Model Tuning")
    print("="*60)
    
    best_lr, baseline_cv_score = tune_logistic_regression(X_train_scaled, y_train)
    
    # Evaluate baseline
    baseline_train_auc, baseline_cv_auc, baseline_cv_scores = evaluate_model_performance(
        best_lr, X_train_scaled, y_train, "Baseline Tuned Model"
    )
    
    # Step 3: Create Multiple Diverse Models
    print("\n" + "="*60)
    print("STEP 3: Creating Multiple Regularized Models")
    print("="*60)
    
    models, predictions, performances = create_diverse_models(X_train_scaled, y_train, X_test_scaled)
    
    # NEW STEP: Feature Selection Summary Table
    print("\n" + "="*60)
    print("STEP 3.5: Feature Selection Analysis")
    print("="*60)
    
    feature_summary, feature_frequency = create_feature_selection_summary(models, performances, X_train_scaled)
    
    # Step 4: Create Weighted Ensemble
    print("\n" + "="*60)
    print("STEP 4: Creating Weighted Ensemble")
    print("="*60)
    
    ensemble_preds, weights = create_weighted_ensemble(predictions, performances)
    
    # Compare all models
    print(f"\n=== Model Performance Comparison ===")
    print(f"Baseline Tuned Model: CV AUC = {baseline_cv_auc:.4f}")
    for i, perf in enumerate(performances):
        print(f"Model {i+1}: CV AUC = {perf:.4f}")
    print(f"Ensemble (weighted): Expected CV AUC ≈ {np.average(performances, weights=weights):.4f}")
    
    # Step 5: Create Final Submission
    print("\n" + "="*60)
    print("STEP 5: Creating Final Submission")
    print("="*60)
    
    # You can choose between ensemble or best single model
    final_predictions = ensemble_preds
    
    submission = pd.DataFrame({
        'id': test_ids,
        'target': final_predictions
    })
    
    submission_file = 'submission5.csv'
    submission.to_csv(submission_file, index=False)
    
    print(f"\n=== Submission Created ===")
    print(f"File saved: {submission_file}")
    print(f"Predictions range: [{final_predictions.min():.4f}, {final_predictions.max():.4f}]")
    print(f"Mean prediction: {final_predictions.mean():.4f}")
    
    # Enhanced Feature analysis with insights from the new table
    print(f"\n=== Enhanced Feature Analysis ===")
    print(f"Each model selected different feature sets:")
    print(f"- Model 1: 10 features (Strong L1) - CV AUC: {performances[0]:.4f}")
    print(f"- Model 2: 20 features (Moderate L2) - CV AUC: {performances[1]:.4f}") 
    print(f"- Model 3: 5 features (Very Strong L1) - CV AUC: {performances[2]:.4f}")
    print(f"- Model 4: 15 features (Balanced L2) - CV AUC: {performances[3]:.4f}")
    
    # Show most important features based on frequency
    top_features = feature_frequency.head(10)
    print(f"\nTop 10 Most Frequently Selected Features:")
    for i, (feature, row) in enumerate(top_features.iterrows(), 1):
        print(f"  {i}. {feature} (selected {row['Times_Selected']} times, avg CV AUC: {row['Avg_CV_AUC']:.4f})")
    
    print(f"\n=== Key Strategy Applied ===")
    print("✓ RobustScaler for outlier-resistant preprocessing")
    print("✓ Multiple regularization strengths (C=0.01 to 1)")
    print("✓ Different feature sets via RFE (5-20 features)")
    print("✓ Both L1 and L2 penalty for diversity")
    print("✓ Weighted ensemble based on CV performance")
    print("✓ Different random seeds for model diversity")
    print("✓ Comprehensive cross-validation evaluation")
    print("✓ Detailed feature selection analysis with performance metrics")

if __name__ == "__main__":
    main()