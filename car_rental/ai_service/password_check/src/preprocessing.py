import pandas as pd
from src.config import (
    PASSWORD_COLUMN,
    TARGET_COLUMN,
    MIN_STRENGTH,
    MAX_STRENGTH,
)


def clean_dataset(df: pd.DataFrame) -> pd.DataFrame:
    # Drop missing values
    df = df.dropna(subset=[PASSWORD_COLUMN, TARGET_COLUMN])

    # Remove empty passwords
    df = df[df[PASSWORD_COLUMN].str.len() > 0]

    # Keep valid strength values only
    df = df[df[TARGET_COLUMN].between(MIN_STRENGTH, MAX_STRENGTH)]

    df = df.drop_duplicates(subset=[PASSWORD_COLUMN])


    return df

