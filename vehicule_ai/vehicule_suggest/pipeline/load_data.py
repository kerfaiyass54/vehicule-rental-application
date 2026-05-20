from pipeline.config import DATA_PATH
import pandas as pd

def load_data():
    df = pd.read_csv(DATA_PATH,
    encoding="utf-8",
    encoding_errors="ignore")
    df.columns = [
        "brand",
        "car_name",
        "engine",
        "battery_capacity",
        "horsepower",
        "top_speed",
        "acceleration_0_100",
        "price",
        "fuel_type",
        "seats",
        "torque"
    ]
    return df