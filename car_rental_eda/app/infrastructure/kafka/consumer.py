import asyncio
import json
import logging
from datetime import datetime, timezone

from aiokafka import AIOKafkaConsumer
from pydantic import ValidationError

from app.application.analysis_runner import AnalysisRunService
from app.infrastructure.config.settings import get_settings
from app.infrastructure.elasticsearch.event_repository import EventRepository
from app.infrastructure.kafka.event_schema import LocationEvent

logger = logging.getLogger(__name__)


async def consume_location_events(
    event_repository: EventRepository,
    analysis_runs: AnalysisRunService,
    stop: asyncio.Event,
) -> None:
    settings = get_settings()
    consumer = AIOKafkaConsumer(
        settings.kafka_location_events_topic,
        bootstrap_servers=settings.kafka_bootstrap_servers,
        group_id=settings.kafka_consumer_group,
        enable_auto_commit=False,
        auto_offset_reset="earliest",
    )
    while not stop.is_set():
        try:
            await consumer.start()
            await asyncio.to_thread(event_repository.ensure_index)
            async for message in consumer:
                if stop.is_set():
                    break
                try:
                    raw_event = json.loads(message.value.decode("utf-8"))
                    event = LocationEvent.model_validate(raw_event)
                    await asyncio.to_thread(
                        event_repository.save,
                        event.event_id,
                        event.model_dump(mode="json"),
                    )
                    await asyncio.to_thread(
                        analysis_runs.run,
                        requested_by=str(event.requested_by) if event.requested_by else None,
                        period_end=datetime.now(timezone.utc),
                        trigger="kafka_event",
                    )
                    await consumer.commit()
                except (ValidationError, ValueError, TypeError, UnicodeDecodeError):
                    logger.exception("Skipping invalid location event at offset %s", message.offset)
                    await consumer.commit()
        except asyncio.CancelledError:
            raise
        except Exception:
            logger.exception("Kafka location event consumer failed; retrying shortly")
            await asyncio.sleep(5)
        finally:
            await consumer.stop()
