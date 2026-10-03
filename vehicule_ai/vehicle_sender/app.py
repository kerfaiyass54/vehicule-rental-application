import re
from pathlib import Path

import pandas as pd
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel, Field
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity


DATASET = Path(__file__).parent / "data" / "vehicles.csv"
VEHICLES = pd.read_csv(DATASET)
TEXT_COLUMNS = ["make", "model", "transmission", "body_style", "fuel_type"]
VEHICLES["search_text"] = VEHICLES[TEXT_COLUMNS].fillna("").astype(str).agg(" ".join, axis=1)
VECTORIZER = TfidfVectorizer(lowercase=True, ngram_range=(1, 2))
DATASET_MATRIX = VECTORIZER.fit_transform(VEHICLES["search_text"])


class VehicleRequest(BaseModel):
    text: str = Field(min_length=3, max_length=4000)


def extract_constraints(text: str) -> dict:
    lower = text.lower()
    budget = re.search(r"(?:under|below|maximum|max|budget(?: of)?|up to)\s*[€$]?\s*([\d.,]+)", lower)
    speed = re.search(r"(?:speed|at least)\s*(\d{2,3})\s*(?:km/?h)?", lower)
    horsepower = re.search(r"(\d{2,4})\s*(?:hp|horsepower)", lower)
    transmission = "AUTOMATIC" if any(word in lower for word in ("automatic", "auto")) else (
        "MANUAL" if "manual" in lower else None
    )
    body_style = next((value for value in ("suv", "sedan", "coupe", "hatchback") if value in lower), None)
    fuel_type = next((value for value in ("electric", "hybrid", "diesel", "petrol", "gasoline") if value in lower), None)
    return {
        "budget": float(budget.group(1).replace(",", "").replace(".", "")) if budget else None,
        "speed": int(speed.group(1)) if speed else None,
        "horsepower": int(horsepower.group(1)) if horsepower else None,
        "transmission": transmission,
        "body_style": body_style,
        "fuel_type": "Petrol" if fuel_type == "gasoline" else fuel_type.title() if fuel_type else None,
    }


def generate_vehicles(prompt: str) -> dict:
    constraints = extract_constraints(prompt)
    query_matrix = VECTORIZER.transform([prompt])
    scores = cosine_similarity(query_matrix, DATASET_MATRIX)[0]
    ranked = VEHICLES.copy()
    ranked["score"] = scores

    if constraints["budget"] is not None:
        ranked["score"] += (ranked["price"] <= constraints["budget"]).astype(float) * 0.35
    if constraints["transmission"]:
        ranked["score"] += (ranked["transmission"] == constraints["transmission"]).astype(float) * 0.3
    if constraints["body_style"]:
        ranked["score"] += (ranked["body_style"].str.lower() == constraints["body_style"]).astype(float) * 0.2
    if constraints["fuel_type"]:
        ranked["score"] += (ranked["fuel_type"].str.lower() == constraints["fuel_type"].lower()).astype(float) * 0.2
    ranked = ranked.sort_values(["score", "price"], ascending=[False, True])

    results = []
    for _, row in ranked.head(10).iterrows():
        reasons = []
        if constraints["budget"] is not None and row["price"] <= constraints["budget"]:
            reasons.append("within the requested budget")
        if constraints["transmission"] == row["transmission"]:
            reasons.append("matches the requested transmission")
        if constraints["body_style"] == row["body_style"].lower():
            reasons.append("matches the requested body style")
        results.append({
            "vehicleName": row["model"],
            "brand": row["make"],
            "color": "To be selected",
            "price": float(row["price"]),
            "maxSpeed": int(row["top_speed"]),
            "transmission": row["transmission"],
            "status": "AVAILABLE",
            "fuelType": row["fuel_type"],
            "horsepower": int(row["horsepower"]),
            "matchScore": round(float(row["score"]) * 100, 2),
            "reason": ", ".join(reasons) or "semantic match from the vehicle dataset",
        })
    return {"interpreted": constraints, "vehicles": results, "dataset": "Car Features and MSRP (Kaggle)"}


app = FastAPI(title="Vehicle Sender")
app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:4200", "http://127.0.0.1:4200"],
    allow_methods=["POST", "OPTIONS"],
    allow_headers=["Authorization", "Content-Type"],
)


@app.post("/vehicle-sender")
def send_vehicle_request(request: VehicleRequest):
    return generate_vehicles(request.text)
