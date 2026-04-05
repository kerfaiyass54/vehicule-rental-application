from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
import joblib
from pathlib import Path


from src.feature_engineering import extract_features


app = FastAPI(title="Password Strength API")

# ✅ ADD CORS HERE
app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:4200"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


BASE_DIR = Path(__file__).resolve().parent.parent

model = joblib.load(BASE_DIR / "models/password_classifier.pkl")
scaler = joblib.load(BASE_DIR / "models/scaler.pkl")


class PasswordRequest(BaseModel):
    password: str


@app.post("/predict")
def predict_strength(data: PasswordRequest):
    features = extract_features(data.password).to_frame().T
    features_scaled = scaler.transform(features)

    prediction = model.predict(features_scaled)[0]
    probabilities = model.predict_proba(features_scaled)[0]

    return {
        "password": data.password,
        "strength": int(prediction),
        "probabilities": {
            "weak": round(probabilities[0], 3),
            "medium": round(probabilities[1], 3),
            "strong": round(probabilities[2], 3),
        },
    }
