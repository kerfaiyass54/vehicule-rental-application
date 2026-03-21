import pandas as pd
from src.preprocessing import clean_dataset
from src.feature_engineering import extract_features
from src.config import PASSWORD_COLUMN, TARGET_COLUMN


def load_dataset(path):
    df = pd.read_csv(path)
    df = clean_dataset(df)

    X = df[PASSWORD_COLUMN].apply(extract_features)
    y = df[TARGET_COLUMN]

    return X, y
