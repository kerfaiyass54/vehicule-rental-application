from pipeline.config import DATA_PATH
import pandas as pd

def load_data():
    return pd.read_csv(DATA_PATH,
    encoding="utf-8",
    encoding_errors="ignore")