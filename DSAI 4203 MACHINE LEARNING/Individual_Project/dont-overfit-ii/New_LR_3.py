import pandas as pd
import numpy as np
from sklearn.linear_model import LogisticRegression
from sklearn.preprocessing import RobustScaler
from sklearn.model_selection import cross_val_score, StratifiedKFold
from sklearn.feature_selection import RFE
from sklearn.metrics import roc_auc_score
from sklearn.ensemble import VotingClassifier, StackingClassifier, RandomForestClassifier
from xgboost import XGBClassifier
import warnings
warnings.filterwarnings('ignore')

RANDOM_SEEDS = [42, 43, 44, 45, 46, 47]  # Reused random states
FEATURE_COUNTS = {  # Feature counts for RFE across models
    'l1': 10,
    'l2': 20,
    'ensemble': 15,
    'base_lr': 12
}
CV_SPLITS = 5 
DATA_PATHS = { 
    'train': 'C:/Users/Zhu Jin Shun/Desktop/DSAI 4203 MACHINE LEARNING/Individual_Project/dont-overfit-ii/train.csv',
    'test': 'C:/Users/Zhu Jin Shun/Desktop/DSAI 4203 MACHINE LEARNING/Individual_Project/dont-overfit-ii/test.csv'
}

def load_data(train_path=DATA_PATHS['train'], test_path=DATA_PATHS['test']):
    """Load and return train/test data with basic checks"""
    train = pd.read_csv(train_path)
    test = pd.read_csv(test_path)
    print(f"Data shapes: Train={train.shape}, Test={test.shape}")
    print(f"Target distribution:\n{train['target'].value_counts()}\n")
    return train, test

def scale_features(df):
    """Simplified scaling (only RobustScaler, as it's prioritized in original code)"""
    scaler = RobustScaler()
    scaled = pd.DataFrame(scaler.fit_transform(df), columns=df.columns)
    print("Features scaled with RobustScaler (resistant to outliers)")
    return scaled

def apply_rfe(model, X, y, n_features, model_name):
    """Simplified RFE with consistent output"""
    selector = RFE(model, n_features_to_select=n_features, step=1)
    selector.fit(X, y)
    X_selected = X[X.columns[selector.support_]]
    print(f"RFE for {model_name}: Selected {n_features} features")
    return X_selected, selector

def evaluate(model, X, y, model_name):
    """Simplified evaluation (focus on key metrics)"""
    cv_scores = cross_val_score(
        model, X, y,
        cv=StratifiedKFold(n_splits=CV_SPLITS, shuffle=True, random_state=RANDOM_SEEDS[0]),
        scoring='roc_auc'
    )
    train_preds = model.predict_proba(X)[:, 1] if hasattr(model, 'predict_proba') else model.predict(X)
    train_auc = roc_auc_score(y, train_preds)
    
    print(f"\n{model_name} Performance:")
    print(f"Training ROC AUC: {train_auc:.4f}")
    print(f"CV ROC AUC: {cv_scores.mean():.4f} (±{cv_scores.std()*2:.4f})")
    return train_auc, cv_scores.mean()

def get_base_models():
    return [
        ('lr_l1', LogisticRegression(
            C=0.1, penalty='l1', solver='liblinear',
            class_weight='balanced', random_state=RANDOM_SEEDS[0], max_iter=2000
        )),
        ('lr_l2', LogisticRegression(
            C=1.0, penalty='l2', class_weight='balanced',
            random_state=RANDOM_SEEDS[1], max_iter=2000
        )),
        ('rf', RandomForestClassifier(
            n_estimators=100, random_state=RANDOM_SEEDS[2], class_weight='balanced'
        )),
        ('xgb', XGBClassifier(
            n_estimators=100, random_state=RANDOM_SEEDS[3], eval_metric='logloss'
        ))
    ]


def create_ensembles(base_models):
    voting = VotingClassifier(estimators=base_models, voting='soft')
    stacking = StackingClassifier(
        estimators=base_models,
        final_estimator=LogisticRegression(
            C=0.5, class_weight='balanced', random_state=RANDOM_SEEDS[4]
        ),
        cv=CV_SPLITS
    )
    return voting, stacking


# --------------------------
def main():

    train, test = load_data()
    X_train_raw = train.drop(columns=['id', 'target'])
    y_train = train['target']
    X_test_raw = test.drop(columns=['id'])
    test_ids = test['id']

    X_train = scale_features(X_train_raw)
    X_test = scale_features(X_test_raw)

    models = []
    predictions = []
    performances = []
    base_models = get_base_models()

    for name, model in base_models[:2]:
        n_feat = FEATURE_COUNTS['l1'] if name == 'lr_l1' else FEATURE_COUNTS['l2']
        X_selected, selector = apply_rfe(model, X_train, y_train, n_feat, name)
        model.fit(X_selected, y_train)
        X_test_selected = X_test[X_selected.columns]
        preds = model.predict_proba(X_test_selected)[:, 1]
        
        _, cv_auc = evaluate(model, X_selected, y_train, f"{name} (RFE {n_feat})")
        models.append((name, model, selector))
        predictions.append(preds)
        performances.append(cv_auc)

    voting_clf, stacking_clf = create_ensembles(base_models)
    X_ensemble, ensemble_selector = apply_rfe(
        LogisticRegression(random_state=RANDOM_SEEDS[5]),
        X_train, y_train, FEATURE_COUNTS['ensemble'], "Ensemble"
    )

    for clf, name in [(voting_clf, "Voting"), (stacking_clf, "Stacking")]:
        clf.fit(X_ensemble, y_train)
        preds = clf.predict_proba(X_test[X_ensemble.columns])[:, 1]
        _, cv_auc = evaluate(clf, X_ensemble, y_train, f"{name} Ensemble")
        models.append((name, clf, ensemble_selector))
        predictions.append(preds)
        performances.append(cv_auc)

    for name, model in base_models[2:]: 
        model.fit(X_train, y_train)  
        preds = model.predict_proba(X_test)[:, 1]
        _, cv_auc = evaluate(model, X_train, y_train, f"Base {name}")
        models.append((name, model, None))
        predictions.append(preds)
        performances.append(cv_auc)

    weights = np.array(performances) / np.sum(performances)
    ensemble_preds = np.average(predictions, axis=0, weights=weights)
    print(f"\nEnsemble Weights:\n{[f'Model {i}: {w:.3f}' for i, w in enumerate(weights)]}")
    
    submission = pd.DataFrame({'id': test_ids, 'target': ensemble_preds})
    submission.to_csv('Final_submission.csv', index=False)
    print(f"\nSubmission saved: Final_submission.csv")


if __name__ == "__main__":
    main()