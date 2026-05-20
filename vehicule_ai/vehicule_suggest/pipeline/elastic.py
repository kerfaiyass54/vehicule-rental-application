from elasticsearch import Elasticsearch

from pipeline.config import (
    ELASTIC_HOST,
    ELASTIC_INDEX
)

es = Elasticsearch(ELASTIC_HOST)


def save_recommendations(
    vehicle_id,
    recommendations
):

    document = {

        "vehicle_id": int(vehicle_id),

        "recommendations": recommendations

    }

    es.index(
        index=ELASTIC_INDEX,
        id=vehicle_id,
        document=document
    )

    print(
        f"Recommendations saved for "
        f"vehicle {vehicle_id}"
    )