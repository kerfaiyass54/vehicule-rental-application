import math
import string
from collections import Counter
import pandas as pd


def extract_features(password: str) -> pd.Series:
    length = len(password)
    digits = sum(c.isdigit() for c in password)
    uppercase = sum(c.isupper() for c in password)
    lowercase = sum(c.islower() for c in password)
    symbols = sum(c in string.punctuation for c in password)
    diversity = len(set(password))
    entropy = diversity / length if length > 0 else 0

    return pd.Series(
        {
            "length": length,
            "digits": digits,
            "uppercase": uppercase,
            "lowercase": lowercase,
            "symbols": symbols,
            "diversity": diversity,
            "entropy": entropy,
        }
    )
