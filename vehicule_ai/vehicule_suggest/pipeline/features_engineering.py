import pickle
import pandas as pd

from sklearn.preprocessing import StandardScaler

from pipeline.clean_data import clean_data
from pipeline.config import (
    FEATURE_COLUMNS,
    SCALER_PATH
)


def remove_extra_data(df):

    df = df[
        (df["seats"] <= 10) &
        (df["horsepower"] <= 1500) &
        (df["top_speed"] <= 450) &
        (df["torque"] <= 3000) &
        (df["price"] <= 1000000) &
        (df["acceleration_0_100"] <= 20)
    ]

    # IMPORTANT FIX
    df = df.reset_index(drop=True)

    return df


def add_features(df):

    # performance score
    df["performance_score"] = (
        df["horsepower"] *
        df["top_speed"]
    ) / df["acceleration_0_100"]

    # value score
    df["value_score"] = (
        df["horsepower"] /
        df["price"]
    )

    # luxury score
    df["luxury_score"] = (
        df["price"] *
        df["top_speed"]
    )

    return df


def set_fuel_features(df):

    df["fuel_type"] = df[
        "fuel_type"
    ].replace({

        # hybrid variants
        "petrol/diesel": "hybrid",
        "diesel/petrol": "hybrid",
        "petrol/hybrid": "hybrid",
        "petrol, hybrid": "hybrid",
        "hybrid (petrol)": "hybrid",
        "plug-in hybrid": "hybrid",
        "hybrid/electric": "hybrid",
        "petrol/ev": "hybrid",

        # petrol variants
        "petrol, diesel": "petrol",
        "petrol/awd": "petrol",

        # extra hybrid variants
        "cng/petrol": "hybrid",
        "hybrid/petrol": "hybrid",
        "hybrid (gas + electric)": "hybrid",
        "gas / hybrid": "hybrid",
        "hybrid / plug-in": "hybrid"

    })

    return df


def encode_labels(df):

    df_encoded = pd.get_dummies(
        df,
        columns=[
            "brand",
            "fuel_type"
        ],
        drop_first=True
    )

    return df_encoded


def scale_features(df):

    scaler = StandardScaler()

    df[FEATURE_COLUMNS] = scaler.fit_transform(
        df[FEATURE_COLUMNS]
    )

    # save scaler
    with open(SCALER_PATH, "wb") as f:

        pickle.dump(scaler, f)

    return df


def drop_features(df):

    features = df.drop(
        columns=[
            "car_name",
            "engine",
            "battery_capacity",
            "luxury_score"
        ],
        errors="ignore"
    )

    return features


def features_engineering():

    # original cleaned dataframe
    df = clean_data()

    # remove outliers
    df = remove_extra_data(df)

    # add engineered features
    df = add_features(df)

    # normalize fuel categories
    df = set_fuel_features(df)

    # encode labels
    df_encoded = encode_labels(df)

    # scale features
    df_scaled = scale_features(df_encoded)

    # keep only model features
    features_df = drop_features(df_scaled)

    return df, features_df