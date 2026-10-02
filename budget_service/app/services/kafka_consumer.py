import json
import asyncio
from aiokafka import AIOKafkaConsumer
from app.core.config import get_settings
from app.services.elasticsearch import create_elasticsearch_client

async def consume_budget_events():
    settings = get_settings()
    consumer = AIOKafkaConsumer(
        settings.kafka_budget_events_topic,
        bootstrap_servers=settings.kafka_bootstrap_servers,
        group_id=settings.kafka_consumer_group,
        value_deserializer=lambda v: json.loads(v.decode('utf-8')))
    
    es_client = create_elasticsearch_client()

    await consumer.start()
    try:
        async for msg in consumer:
            event_data = msg.value
            await asyncio.to_thread(
                es_client.index,
                index=settings.budget_history_index,
                document=event_data
            )
    finally:
        await consumer.stop()
        es_client.close()