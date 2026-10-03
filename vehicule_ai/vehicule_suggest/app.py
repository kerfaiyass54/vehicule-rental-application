import os
from contextlib import asynccontextmanager
from datetime import datetime, timezone

from aiokafka import AIOKafkaProducer
from fastapi import FastAPI, HTTPException
from sklearn.metrics.pairwise import cosine_similarity
import numpy as np
from pydantic import BaseModel, EmailStr
from sqlalchemy import create_engine, text


DATABASE_URL = os.getenv(
    "DATABASE_URL",
    "postgresql+psycopg2://postgres:krkrfrang@localhost:5580/vehiculerents",
)
KAFKA_BOOTSTRAP_SERVERS = os.getenv("KAFKA_BOOTSTRAP_SERVERS", "localhost:9194")
RESULT_TOPIC = os.getenv("KAFKA_RECOMMENDATION_RESULTS_TOPIC", "recommendation_results")
engine = create_engine(DATABASE_URL, pool_pre_ping=True)
producer: AIOKafkaProducer | None = None


class RecommendationRequest(BaseModel):
    email: EmailStr


def build_recommendation(email: str) -> dict:
    with engine.begin() as connection:
        client = connection.execute(
            text("""
                SELECT c.id_client, c.budget, c.id_location, l.location_name
                FROM clients c
                JOIN locations l ON l.id_location = c.id_location
                WHERE LOWER(c.email) = LOWER(:email)
            """),
            {"email": email},
        ).mappings().first()
        if client is None:
            raise HTTPException(status_code=404, detail="Client not found")

        purchases = connection.execute(
            text("""
                SELECT v.transmission, v.max_speed, v.price, s.id_supplier
                FROM buyings b
                JOIN vehicles v ON v.id_vehicle = b.id_vehicle
                JOIN suppliers s ON s.id_supplier = b.id_supplier
                WHERE b.id_client = :client_id
                ORDER BY b.date_buy DESC
            """),
            {"client_id": client["id_client"]},
        ).mappings().all()

        transmission_counts: dict[str, int] = {}
        for purchase in purchases:
            transmission = purchase["transmission"]
            transmission_counts[transmission] = transmission_counts.get(transmission, 0) + 1
        preferred_transmission = (
            max(transmission_counts, key=transmission_counts.get)
            if transmission_counts else None
        )
        preferred_speed = (
            sum(row["max_speed"] for row in purchases if row["max_speed"] is not None)
            / len([row for row in purchases if row["max_speed"] is not None])
            if any(row["max_speed"] is not None for row in purchases) else None
        )
        history_vectors = [
            [
                float(row["price"] or 0) / max(float(client["budget"] or 1), 1),
                float(row["max_speed"] or 0) / 500,
                1.0 if row["transmission"] == "AUTOMATIC" else 0.0,
                1.0 if row["transmission"] == "MANUAL" else 0.0,
            ]
            for row in purchases
        ]

        rows = connection.execute(
            text("""
                SELECT s.id_supplier, s.supplier_name, a.road, a.number,
                       v.id_vehicle, v.vehicle_name, v.brand, v.price,
                       v.max_speed, v.transmission, v.vehicle_status
                FROM addresses a
                JOIN suppliers s ON s.id_supplier = a.id_supplier
                JOIN vehicles v ON v.id_supplier = s.id_supplier
                WHERE a.id_location = :location_id
                  AND v.vehicle_status = 'AVAILABLE'
                ORDER BY s.supplier_name, v.price
            """),
            {"location_id": client["id_location"]},
        ).mappings().all()

    budget = float(client["budget"] or 0)
    grouped: dict[int, dict] = {}
    for row in rows:
        price = float(row["price"])
        speed = float(row["max_speed"] or 0)
        score = 0.0
        reasons = []
        if budget > 0:
            score += max(0.0, 45.0 - abs(price - budget) / budget * 45.0)
            if price <= budget:
                score += 20.0
                reasons.append("within your budget")
        if preferred_transmission and row["transmission"] == preferred_transmission:
            score += 20.0
            reasons.append("matches your preferred transmission")
        if preferred_speed is not None:
            score += max(0.0, 10.0 - abs(speed - preferred_speed) / max(preferred_speed, 1) * 10.0)
            if speed >= preferred_speed:
                reasons.append("matches your previous speed preference")
        if history_vectors:
            candidate_vector = np.array([[
                price / max(budget, 1),
                speed / 500,
                1.0 if row["transmission"] == "AUTOMATIC" else 0.0,
                1.0 if row["transmission"] == "MANUAL" else 0.0,
            ]])
            history_similarity = cosine_similarity(
                candidate_vector, np.array(history_vectors).mean(axis=0, keepdims=True)
            )[0][0]
            score += max(0.0, history_similarity) * 20.0
            reasons.append("learned from your purchase history")
        if any(row["id_supplier"] == purchase["id_supplier"] for purchase in purchases):
            score += 5.0
            reasons.append("from a supplier you used before")
        if not reasons:
            reasons.append("available at your nearest supplier")

        supplier = grouped.setdefault(row["id_supplier"], {
            "supplierId": row["id_supplier"],
            "supplierName": row["supplier_name"],
            "road": row["road"],
            "addressNumber": row["number"],
            "vehicles": [],
        })
        supplier["vehicles"].append({
            "vehicleId": row["id_vehicle"],
            "vehicleName": row["vehicle_name"],
            "brand": row["brand"],
            "price": price,
            "maxSpeed": row["max_speed"],
            "transmission": row["transmission"],
            "status": row["vehicle_status"],
            "score": round(score, 2),
            "reason": ", ".join(reasons),
        })

    suppliers = []
    for supplier in grouped.values():
        supplier["vehicles"].sort(key=lambda vehicle: (-vehicle["score"], vehicle["price"]))
        supplier["vehicles"] = supplier["vehicles"][:5]
        suppliers.append(supplier)
    suppliers.sort(key=lambda supplier: -supplier["vehicles"][0]["score"] if supplier["vehicles"] else 0)
    return {
        "email": email,
        "locationName": client["location_name"],
        "budget": budget,
        "suppliers": suppliers,
        "generatedAt": datetime.now(timezone.utc).isoformat(),
    }


@asynccontextmanager
async def lifespan(_app: FastAPI):
    global producer
    producer = AIOKafkaProducer(bootstrap_servers=KAFKA_BOOTSTRAP_SERVERS)
    await producer.start()
    yield
    await producer.stop()


app = FastAPI(title="Vehicle Recommendation Service", lifespan=lifespan)


@app.post("/recommendations", status_code=202)
async def recommend(request: RecommendationRequest):
    if producer is None:
        raise HTTPException(status_code=503, detail="Kafka producer is not ready")
    result = build_recommendation(str(request.email))
    await producer.send_and_wait(
        RESULT_TOPIC,
        key=str(request.email).lower().encode(),
        value=__import__("json").dumps(result).encode(),
    )
    return {"status": "published", "email": request.email}
