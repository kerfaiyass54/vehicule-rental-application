from pipeline.clean_data import clean_data
import pandas as pd
from sklearn.preprocessing import StandardScaler


def remove_extra_data(df):
    df = df[
        (df["seats"] <= 10) &
        (df["horsepower"] <= 1500) &
        (df["top_speed"] <= 450) &
        (df["torque"] <= 3000) &
        (df["price"] <= 1000000) &
        (df["acceleration_0_100"] <= 20)
        ]

    return df

def add_features(df):
    df["performance_score"] = (
                                      df["horsepower"] *
                                      df["top_speed"]
                              ) / df["acceleration_0_100"]

    df["value_score"] = (
            df["horsepower"] /
            df["price"]
    )

    df["luxury_score"] = (
            df["price"] *
            df["top_speed"]
    )

    return df

def set_fuel_features(df):
    df["fuel_type"] = df["fuel_type"].replace({

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
        "petrol/awd": "petrol"

    })

    df["fuel_type"] = df["fuel_type"].replace({

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
        columns=["brand", "fuel_type"],
        drop_first=True
    )
    return df_encoded

def scale_features(df):
    scaler = StandardScaler()

    numerical_cols = [
        "horsepower",
        "top_speed",
        "acceleration_0_100",
        "price",
        "torque",
        "seats",
        "performance_score",
        "value_score"
    ]

    df[numerical_cols] = scaler.fit_transform(
        df[numerical_cols]
    )
    return df

def drop_features(df):
    features = df.drop(
        columns=[
            "car_name",
            "engine",
            "battery_capacity"
        ]
    )
    return features

def features_engineering():
    df = clean_data()
    df = remove_extra_data(df)
    df = add_features(df)
    df = set_fuel_features(df)
    df = encode_labels(df)
    df = scale_features(df)
    df = drop_features(df)
    return df