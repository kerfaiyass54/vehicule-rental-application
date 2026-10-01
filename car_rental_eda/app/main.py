import asyncio
from contextlib import asynccontextmanager

from fastapi import FastAPI

from app.api.routes import router
from app.application.analysis_runner import AnalysisRunService
from app.domain.location_analysis import LocationAnalysisService
from app.infrastructure.config.settings import get_settings
from app.infrastructure.elasticsearch.analysis_repository import AnalysisRepository
from app.infrastructure.elasticsearch.client import create_elasticsearch_client
from app.infrastructure.elasticsearch.event_repository import EventRepository
from app.infrastructure.kafka.consumer import consume_location_events


@asynccontextmanager
async def lifespan(app: FastAPI):
    client = create_elasticsearch_client()
    analysis_repository = AnalysisRepository(client)
    event_repository = EventRepository(client)
    run_service = AnalysisRunService(
        LocationAnalysisService(),
        event_repository,
        analysis_repository,
    )

    app.state.analysis_runs = run_service

    stop_consumer = asyncio.Event()
    consumer_task = None
    settings = get_settings()
    try:
        await asyncio.to_thread(event_repository.ensure_index)
        await asyncio.to_thread(run_service.run, trigger="startup")
        if settings.kafka_enabled:
            consumer_task = asyncio.create_task(
                consume_location_events(event_repository, run_service, stop_consumer)
            )
        yield
    finally:
        stop_consumer.set()
        if consumer_task:
            consumer_task.cancel()
            try:
                await consumer_task
            except asyncio.CancelledError:
                pass
        client.close()


app = FastAPI(title="Car Rental Location EDA API", lifespan=lifespan)
app.include_router(router)
