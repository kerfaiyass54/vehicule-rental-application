import pandas as pd
import numpy as np
import re

from pipeline.load_data import load_data
from pipeline.config import NUMERIC_COLUMNS


def drop_nulls(df):

    df.dropna(inplace=True)

    return df


def drop_duplicates(df):

    df.drop_duplicates(inplace=True)

    return df


def normalize_text(df):

    text_cols = [
        "brand",
        "fuel_type",
        "engine",
        "car_name"
    ]

    for col in text_cols:

        df[col] = (
            df[col]
            .astype(str)
            .str.lower()
            .str.strip()
        )

    return df


def clean_numeric(value):

    if pd.isna(value):
        return np.nan

    value = str(value).strip()

    # fix weird decimal spacing
    value = value.replace(". ", ".")

    # remove commas and dollar signs
    value = value.replace(",", "")
    value = value.replace("$", "")

    # ranges like 70-85
    if "-" in value:

        parts = re.findall(r"\d+\.?\d*", value)

        if len(parts) >= 2:

            nums = [float(x) for x in parts]

            return sum(nums) / len(nums)

    # seat format like 2+2
    if "+" in value:

        parts = re.findall(r"\d+", value)

        if len(parts) >= 2:

            nums = [int(x) for x in parts]

            return sum(nums)

    # extract numeric value
    match = re.search(r"\d+\.?\d*", value)

    if match:

        return float(match.group())

    return np.nan


def set_to_float(df):

    for col in NUMERIC_COLUMNS:

        df[col] = df[col].apply(clean_numeric)

    df[NUMERIC_COLUMNS] = df[
        NUMERIC_COLUMNS
    ].astype(float)

    return df


def clean_data():

    df = load_data()

    df = drop_nulls(df)

    df = drop_duplicates(df)

    df = normalize_text(df)

    df = set_to_float(df)

    return df