DATA_PATH = "./data/data.csv"

MODEL_PATH = "./data/similarity.pkl"

VEHICLES_PATH = "./data/vehicles.pkl"

SCALER_PATH = "./data/scaler.pkl"

COLUMNS = [
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

NUMERIC_COLUMNS = [
    "horsepower",
    "top_speed",
    "price",
    "acceleration_0_100",
    "torque",
    "seats"
]

FEATURE_COLUMNS = [
    "horsepower",
    "top_speed",
    "acceleration_0_100",
    "price",
    "torque",
    "seats",
    "performance_score",
    "value_score"
]

# PostgreSQL
DB_HOST = "localhost"
DB_PORT = 5432
DB_NAME = "vehiculerents"
DB_USER = "postgres"
DB_PASSWORD = "krkrfrang"

# Elasticsearch
ELASTIC_HOST = "http://localhost:9200"
ELASTIC_INDEX = "vehicle_recommendations"