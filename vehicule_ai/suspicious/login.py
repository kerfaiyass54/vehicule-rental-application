from fastapi import FastAPI
from pydantic import BaseModel
from elasticsearch import Elasticsearch
import pandas as pd
import numpy as np
from sklearn.ensemble import IsolationForest

app = FastAPI()
es = Elasticsearch("http://localhost:9200")  # Elasticsearch host

class UserRequest(BaseModel):
    email: str

@app.post("/check-suspicious")
def check_suspicious(request: UserRequest):
    query = {
        "query": {"match": {"email": request.email}},
        "size": 1000
    }
    res = es.search(index="user_login_sessions", body=query)

    if not res["hits"]["hits"]:
        return {"suspicious": []}
    df = pd.DataFrame([hit["_source"] for hit in res["hits"]["hits"]])

    df["sessionStart_ts"] = pd.to_datetime(df["sessionStart"]).astype(np.int64) // 10**9

    clf = IsolationForest(contamination=0.05, random_state=42)
    df["anomaly"] = clf.fit_predict(df[["sessionStart_ts"]])

    suspicious = df[df["anomaly"] == -1].to_dict(orient="records")
    return {"suspicious": suspicious}
