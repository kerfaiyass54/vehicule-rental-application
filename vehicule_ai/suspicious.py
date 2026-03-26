from fastapi import FastAPI
from pydantic import BaseModel
import numpy as np
from sklearn.ensemble import IsolationForest

app = FastAPI()

model = IsolationForest(
    n_estimators=100,
    contamination=0.05,
    random_state=42
)

X_train = np.array([
    [1, 1, 14],
    [1, 1, 13],
    [1, 1, 15]
])
model.fit(X_train)

class BehaviorRequest(BaseModel):
    countryCount: int
    deviceCount: int
    loginHour: int

@app.post("/predict")
def predict(req: BehaviorRequest):
    X = np.array([[req.countryCount, req.deviceCount, req.loginHour]])

    score = model.decision_function(X)[0]
    anomaly = model.predict(X)[0] == -1

    return {
        "riskScore": float(abs(score)),   # ✅ numpy.float → float
        "suspicious": bool(anomaly)       # ✅ numpy.bool → bool
    }

