from pipeline.load_data import load_data
from pipeline.config import NUMERIC_COLUMNS
import pandas as pd
import numpy as np
import re


def drop_nulls(df):
    df.dropna(inplace=True)
    return df


def drop_duplicates(df):
    df.drop_duplicates(inplace=True)
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

    # handle ranges like 70-85 or 100 - 140
    if "-" in value:

        parts = re.findall(r"\d+\.?\d*", value)

        if len(parts) >= 2:
            nums = [float(x) for x in parts]
            return sum(nums) / len(nums)

    # handle seat format like 2+2
    if "+" in value:

        parts = re.findall(r"\d+", value)

        if len(parts) >= 2:
            nums = [int(x) for x in parts]
            return sum(nums)

    # extract first numeric value
    match = re.search(r"\d+\.?\d*", value)

    if match:
        return float(match.group())

    return np.nan


def set_to_float(df):
    for i in NUMERIC_COLUMNS:
        df[i] = df[i].apply(clean_numeric)
    df[NUMERIC_COLUMNS] = df[NUMERIC_COLUMNS].astype(float)
    return df

def clean_data():
    df = load_data()
    df = drop_nulls(df)
    df = drop_duplicates(df)
    df = set_to_float(df)
    return df