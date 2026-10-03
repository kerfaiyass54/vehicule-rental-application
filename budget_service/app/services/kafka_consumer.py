import json
import asyncio
import logging
from datetime import datetime, timezone
from aiokafka import AIOKafkaConsumer
from app.core.config import get_settings
from app.services.elasticsearch import create_elasticsearch_client

logger = logging.getLogger(__name__)

def normalize_event(event_data: dict) -> dict:
    event = dict(event_data)
    date = event.get("date")
    if isinstance(date, (int, float)):
        event["date"] = datetime.fromtimestamp(date, tz=timezone.utc).isoformat()
    return event

async def consume_budget_events():
    settings = get_settings()
    consumer = AIOKafkaConsumer(
        settings.kafka_budget_events_topic,
        bootstrap_servers=settings.kafka_bootstrap_servers,
        group_id=settings.kafka_consumer_group,
        enable_auto_commit=False,
        auto_offset_reset="earliest",
        value_deserializer=lambda v: json.loads(v.decode("utf-8")),
    )

    while True:
        es_client = create_elasticsearch_client()
        try:
            await consumer.start()
            logger.info("Budget event consumer started for topic %s", settings.kafka_budget_events_topic)
            async for msg in consumer:
                try:
                    event_data = normalize_event(msg.value)
                    await asyncio.to_thread(
                        es_client.index,
                        index=settings.budget_history_index,
                        document=event_data,
                    )
                    await consumer.commit()
                except Exception:
                    logger.exception("Failed to index budget event at offset %s; retrying", msg.offset)
                    await asyncio.sleep(2)
        except asyncio.CancelledError:
            raise
        except Exception:
            logger.exception("Budget event consumer failed; retrying shortly")
            await asyncio.sleep(5)
        finally:
            await consumer.stop()
            es_client.close()