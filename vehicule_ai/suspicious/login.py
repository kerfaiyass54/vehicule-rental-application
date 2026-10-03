import os
from fastapi import FastAPI
from pydantic import BaseModel
from elasticsearch import Elasticsearch
import pandas as pd
import numpy as np
from sklearn.ensemble import IsolationForest

app = FastAPI()
elastic_url = os.getenv("ELASTICSEARCH_URL", "http://localhost:9200")
elastic_password = os.getenv("ELASTICSEARCH_PASSWORD") or os.getenv("ELASTIC_PASSWORD")
options = {"basic_auth": ("elastic", elastic_password)} if elastic_password else {}
es = Elasticsearch(elastic_url, **options)
SESSION_INDEX = os.getenv("ELASTICSEARCH_SESSION_INDEX", "account-login-sessions")

class UserRequest(BaseModel):
    email: str

@app.post("/check-suspicious")
def check_suspicious(request: UserRequest):
    query = {
        "query": {"match": {"email": request.email}},
        "size": 1000
    }
    res = es.search(index=SESSION_INDEX, body=query)

    if not res["hits"]["hits"]:
        return {"suspicious": []}
    df = pd.DataFrame([hit["_source"] for hit in res["hits"]["hits"]])

    df["sessionStart_ts"] = pd.to_datetime(df["sessionStart"]).astype(np.int64) // 10**9

    clf = IsolationForest(contamination=0.05, random_state=42)
    df["anomaly"] = clf.fit_predict(df[["sessionStart_ts"]])

    suspicious = df[df["anomaly"] == -1].to_dict(orient="records")
    return {"suspicious": suspicious}
