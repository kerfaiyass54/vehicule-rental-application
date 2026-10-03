from datetime import datetime, timezone
import os
from pathlib import Path
import re
from uuid import uuid4

import pandas as pd
from elasticsearch import Elasticsearch
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel, Field
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity


BASE_DIR = Path(__file__).parent
LINE_ITEMS_FILE = BASE_DIR / "data" / "data" / "line_items.csv"
SERVICE_RECORDS_FILE = BASE_DIR / "data" / "data" / "service_records.csv"
ELASTIC_INDEX = os.getenv("ELASTICSEARCH_REPAIR_INDEX", "vehicle-repair-estimates")


def load_training_rows() -> pd.DataFrame:
    line_items = pd.read_csv(LINE_ITEMS_FILE)
    service_records = pd.read_csv(SERVICE_RECORDS_FILE)
    service_records["request_opened"] = pd.to_datetime(
        service_records["request_opened"], errors="coerce"
    )
    service_records["request_ready"] = pd.to_datetime(
        service_records["request_ready"], errors="coerce"
    )
    service_records["duration_minutes"] = (
        service_records["request_ready"] - service_records["request_opened"]
    ).dt.total_seconds().div(60)
    service_records["duration_minutes"] = service_records["duration_minutes"].clip(
        lower=15, upper=480
    )
    rows = line_items.merge(
        service_records[["service_id", "duration_minutes"]],
        on="service_id",
        how="left",
    )
    rows["description"] = rows["description"].fillna("").astype(str)
    rows["service_type"] = rows["service_type"].fillna("General inspection")
    rows["search_text"] = (
        rows["service_type"].astype(str) + " " + rows["description"]
    )
    # A visit can contain several line items, so distribute the observed
    # shop time across its tasks before learning task-level estimates.
    item_counts = rows.groupby("service_id")["service_id"].transform("count")
    rows["task_minutes"] = (rows["duration_minutes"] / item_counts).clip(
        lower=15, upper=240
    )
    return rows


TRAINING_ROWS = load_training_rows()
VECTORIZER = TfidfVectorizer(lowercase=True, ngram_range=(1, 2))
TRAINING_MATRIX = VECTORIZER.fit_transform(TRAINING_ROWS["search_text"])


def create_elasticsearch_client() -> Elasticsearch:
    url = os.getenv("ELASTICSEARCH_URL", "http://localhost:9200")
    password = os.getenv("ELASTICSEARCH_PASSWORD") or os.getenv("ELASTIC_PASSWORD")
    options = {"basic_auth": ("elastic", password)} if password else {}
    return Elasticsearch(url, **options)


ELASTICSEARCH = create_elasticsearch_client()


def ensure_index() -> None:
    if ELASTICSEARCH.indices.exists(index=ELASTIC_INDEX):
        return
    ELASTICSEARCH.indices.create(
        index=ELASTIC_INDEX,
        mappings={
            "properties": {
                "description": {"type": "text"},
                "tasks": {"type": "object", "enabled": True},
                "totalEstimatedMinutes": {"type": "integer"},
                "createdAt": {"type": "date"},
                "dataset": {"type": "keyword"},
            }
        },
    )


class RepairEstimateRequest(BaseModel):
    description: str = Field(min_length=3, max_length=4000)
    ticketType: str | None = Field(default=None, max_length=50)
    vehicleName: str | None = Field(default=None, max_length=150)


def fallback_task(description: str) -> dict:
    lower = description.lower()
    keywords = [
        ("brake", "Brake system diagnosis and repair", 90),
        ("oil", "Oil and filter service", 60),
        ("tire", "Tire inspection and rotation", 45),
        ("wheel", "Wheel and alignment inspection", 60),
        ("battery", "Battery and charging-system test", 45),
        ("engine", "Engine diagnostic inspection", 120),
        ("light", "Lighting system inspection", 30),
    ]
    for keyword, task, minutes in keywords:
        if keyword in lower:
            return {
                "task": task,
                "estimatedMinutes": minutes,
                "confidence": 0.45,
                "sourceDescription": "Rule-assisted estimate; no close dataset match",
            }
    return {
        "task": "General vehicle diagnostic inspection",
        "estimatedMinutes": 90,
        "confidence": 0.35,
        "sourceDescription": "Rule-assisted estimate; no close dataset match",
    }


def estimate_tasks(request: RepairEstimateRequest) -> dict:
    query = " ".join(
        value for value in (request.ticketType, request.vehicleName, request.description)
        if value
    )
    scores = cosine_similarity(VECTORIZER.transform([query]), TRAINING_MATRIX)[0]
    ranked = TRAINING_ROWS.copy()
    ranked["similarity"] = scores
    matches = ranked[ranked["similarity"] >= 0.08].sort_values(
        "similarity", ascending=False
    ).head(4)

    if matches.empty:
        tasks = [fallback_task(request.description)]
    else:
        tasks = []
        seen_types: set[str] = set()
        for _, row in matches.iterrows():
            task_type = str(row["service_type"])
            if task_type in seen_types:
                continue
            seen_types.add(task_type)
            tasks.append(
                {
                    "task": task_type,
                    "estimatedMinutes": int(round(float(row["task_minutes"]))),
                    "confidence": round(min(float(row["similarity"]) * 1.8, 0.99), 2),
                    "sourceDescription": str(row["description"]),
                }
            )

    total = sum(task["estimatedMinutes"] for task in tasks)
    result = {
        "description": request.description,
        "tasks": tasks,
        "totalEstimatedMinutes": total,
        "dataset": "My Car's Maintenance Diary: Real Service Cost Data (Kaggle v4)",
        "datasetUrl": (
            "https://www.kaggle.com/datasets/josephnehrenz/"
            "my-cars-maintenance-diary-real-service-cost-data"
        ),
    }
    ensure_index()
    document = {
        **result,
        "ticketType": request.ticketType,
        "vehicleName": request.vehicleName,
        "createdAt": datetime.now(timezone.utc).isoformat(),
    }
    ELASTICSEARCH.index(index=ELASTIC_INDEX, id=str(uuid4()), document=document)
    return result


app = FastAPI(title="Vehicle Repair Estimator")
app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:4200", "http://127.0.0.1:4200"],
    allow_methods=["POST", "OPTIONS"],
    allow_headers=["Authorization", "Content-Type"],
)


@app.post("/repair-estimates")
def create_repair_estimate(request: RepairEstimateRequest) -> dict:
    return estimate_tasks(request)
