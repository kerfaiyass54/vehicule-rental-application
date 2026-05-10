from pipeline.config import DATA_PATH
import pandas as pd

def load_data():
    return pd.read_csv(DATA_PATH)