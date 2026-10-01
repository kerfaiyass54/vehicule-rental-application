from datetime import datetime
from typing import Any

from elasticsearch import Elasticsearch

from app.infrastructure.config.settings import get_settings


class EventRepository:
    """Elasticsearch adapter for location event history."""

    def __init__(self, client: Elasticsearch) -> None:
        self.client = client
        self.index = get_settings().elasticsearch_events_index

    def ensure_index(self) -> None:
        if self.client.indices.exists(index=self.index):
            return
        self.client.indices.create(
            index=self.index,
            mappings={"properties": {
                "event_id": {"type": "keyword"},
                "event_type": {"type": "keyword"},
                "entity_type": {"type": "keyword"},
                "entity_id": {"type": "keyword"},
                "occurred_at": {"type": "date"},
                "location_id": {"type": "keyword"},
                "previous_location_id": {"type": "keyword"},
                "new_location_id": {"type": "keyword"},
                "location_name": {"type": "keyword"},
                "country": {"type": "keyword"},
                "latitude": {"type": "float"},
                "longitude": {"type": "float"},
            }},
        )

    def find_between(self, start: datetime, end: datetime) -> list[dict[str, Any]]:
        return self._search(
            {"range": {"occurred_at": {"gte": start.isoformat(), "lte": end.isoformat()}}},
            "asc",
        )

    def find_as_of(self, end: datetime) -> list[dict[str, Any]]:
        return self._search({"range": {"occurred_at": {"lte": end.isoformat()}}}, "desc")

    def save(self, event_id: str, document: dict[str, Any]) -> None:
        self.client.index(
            index=self.index,
            id=event_id,
            document=document,
            refresh="wait_for",
        )

    def _search(self, query: dict[str, Any], order: str) -> list[dict[str, Any]]:
        response = self.client.search(
            index=self.index,
            size=10000,
            query=query,
            sort=[{"occurred_at": order}],
        )
        return [hit["_source"] for hit in response["hits"]["hits"]]
