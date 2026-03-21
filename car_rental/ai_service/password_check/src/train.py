import joblib
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from sklearn.ensemble import RandomForestClassifier
from sklearn.model_selection import GridSearchCV
from src.data_loader import load_dataset
from src.config import (
    DATA_PATH,
    TEST_SIZE,
    RANDOM_STATE,
    MODEL_PATH,
    SCALER_PATH,
)


def train():
    X, y = load_dataset(DATA_PATH)
    X_train, X_test, y_train, y_test = train_test_split(
        X,
        y,
        test_size=TEST_SIZE,
        random_state=RANDOM_STATE,
        stratify=y,
    )

    scaler = StandardScaler()
    X_train_scaled = scaler.fit_transform(X_train)
    X_test_scaled = scaler.transform(X_test)

    print(set(X_train.index).intersection(set(X_test.index)))


    param_grid = {
    "n_estimators": [100],
    "max_depth": [5, 10],
    "min_samples_split": [5, 10],
    "min_samples_leaf": [2, 5],
}


    rf = RandomForestClassifier(
        random_state=RANDOM_STATE,
        class_weight="balanced",
        n_jobs=-1,
    )

    grid = GridSearchCV(
        rf,
        param_grid,
        cv=3,
        scoring="f1_weighted",
        n_jobs=-1,
        verbose=1,
    )

    grid.fit(X_train_scaled, y_train)

    best_model = grid.best_estimator_

    joblib.dump(best_model, MODEL_PATH)
    joblib.dump(scaler, SCALER_PATH)

    print("Best params:", grid.best_params_)

    return best_model, X_test_scaled, y_test

