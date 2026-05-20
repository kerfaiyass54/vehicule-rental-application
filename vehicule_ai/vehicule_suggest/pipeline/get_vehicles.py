import pandas as pd

from pipeline.db import engine


def get_all_vehicles():

    query = """
    SELECT *
    FROM vehicule
    """

    df = pd.read_sql(
        query,
        engine
    )

    return df