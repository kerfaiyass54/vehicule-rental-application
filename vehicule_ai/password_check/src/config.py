# Dataset
DATA_PATH = "datasets/data.csv"
DATA_PATH_CLEAN = "datasets/data_init.csv"


# Columns
PASSWORD_COLUMN = "password"
TARGET_COLUMN = "strength"

# Strength range
MIN_STRENGTH = 0
MAX_STRENGTH = 2

# Train / test
TEST_SIZE = 0.2
RANDOM_STATE = 42

# Model saving
MODEL_PATH = "models/password_classifier.pkl"
SCALER_PATH = "models/scaler.pkl"
