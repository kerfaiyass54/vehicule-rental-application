import pandas as pd

from pipeline.db import engine


def get_supplier_vehicles(
        supplier_id: int
):

    query = f"""
    SELECT *
    FROM vehicule
    WHERE id_supp = {supplier_id}
    """

    df = pd.read_sql(
        query,
        engine
    )

    return df