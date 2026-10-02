from fastapi import FastAPI
from elasticsearch import AsyncElasticsearch
import asyncio
import json
from aiokafka import AIOKafkaConsumer
from datetime import datetime

app = FastAPI()
es = AsyncElasticsearch("http://localhost:9200")

BUDGET_HISTORY_INDEX = "budget_history"
KAFKA_TOPIC = "budget_events"
KAFKA_BOOTSTRAP_SERVERS = "localhost:9092"

async def create_index_if_not_exists():
    if not await es.indices.exists(index=BUDGET_HISTORY_INDEX):
        await es.indices.create(
            index=BUDGET_HISTORY_INDEX,
            body={
                "mappings": {
                    "properties": {
                        "email": {"type": "keyword"},
                        "budget": {"type": "float"},
                        "date": {"type": "date"},
                        "event_type": {"type": "keyword"},
                    }
                }
            },
        )

async def consume_budget_events():
    consumer = AIOKafkaConsumer(
        KAFKA_TOPIC,
        bootstrap_servers=KAFKA_BOOTSTRAP_SERVERS,
        value_deserializer=lambda m: json.loads(m.decode("utf-8")),
    )
    await consumer.start()
    try:
        async for msg in consumer:
            event_data = msg.value
            await es.index(
                index=BUDGET_HISTORY_INDEX,
                document={
                    "email": event_data["email"],
                    "budget": event_data["budget"],
                    "date": datetime.now(),
                    "event_type": event_data["type"],
                },
            )
    finally:
        await consumer.stop()


@app.on_event("startup")
async def startup_event():
    await create_index_if_not_exists()
    asyncio.create_task(consume_budget_events())


@app.get("/health")
def health_check():
    return {"status": "ok"}


@app.get("/budget/history")
async def get_budget_history(
    email: str,
    start_date: str,
    end_date: str,
    page: int = 1,
    size: int = 10,
):
    query = {
        "bool": {
            "must": [
                {"match": {"email": email}},
                {
                    "range": {
                        "date": {
                            "gte": start_date,
                            "lte": end_date,
                        }
                    }
                },
            ]
        }
    }
    res = await es.search(
        index=BUDGET_HISTORY_INDEX,
        query=query,
        from_=(page - 1) * size,
        size=size,
        sort=[{"date": "desc"}],
    )
    return {
        "total": res["hits"]["total"]["value"],
        "page": page,
        "size": size,
        "hits": [hit["_source"] for hit in res["hits"]["hits"]],
    }