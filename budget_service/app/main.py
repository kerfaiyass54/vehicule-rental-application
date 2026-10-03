import asyncio
from contextlib import asynccontextmanager

from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.api.endpoints import budget
from app.core.config import get_settings
from app.services.elasticsearch import create_elasticsearch_client
from app.services.kafka_consumer import consume_budget_events

@asynccontextmanager
async def lifespan(app: FastAPI):
    settings = get_settings()
    es_client = create_elasticsearch_client()

    # Create the index if it doesn't exist
    if not await asyncio.to_thread(es_client.indices.exists, index=settings.budget_history_index):
        await asyncio.to_thread(
            es_client.indices.create,
            index=settings.budget_history_index,
            body={
                "mappings": {
                    "properties": {
                        "email": {"type": "keyword"},
                        "budget": {"type": "float"},
                        "previous_budget": {"type": "float"},
                        "date": {"type": "date"},
                        "event_type": {"type": "keyword"},
                        "entity_type": {"type": "keyword"},
                        "entity_id": {"type": "keyword"},
                    }
                }
            },
        )

    stop_consumer = asyncio.Event()
    consumer_task = None
    if settings.kafka_enabled:
        consumer_task = asyncio.create_task(consume_budget_events())
    
    yield
    
    stop_consumer.set()
    if consumer_task:
        consumer_task.cancel()
        try:
            await consumer_task
        except asyncio.CancelledError:
            pass
    es_client.close()

app = FastAPI(title="Budget Service", lifespan=lifespan)
app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:4200", "http://127.0.0.1:4200"],
    allow_methods=["GET", "OPTIONS"],
    allow_headers=["Authorization", "Content-Type"],
)
app.include_router(budget.router, prefix="/budget", tags=["budget"])