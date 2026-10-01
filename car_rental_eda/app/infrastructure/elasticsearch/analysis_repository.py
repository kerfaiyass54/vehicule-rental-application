from datetime import datetime, timezone
from typing import Any

from elasticsearch import Elasticsearch

from app.infrastructure.config.settings import get_settings


class AnalysisRepository:
    def __init__(self, client: Elasticsearch) -> None:
        self.client = client
        self.index = get_settings().elasticsearch_analysis_index

    def create_index_if_missing(self) -> None:
        if self.client.indices.exists(index=self.index):
            return

        self.client.indices.create(
            index=self.index,
            mappings={
                "properties": {
                    "requested_by": {"type": "keyword"},
                    "created_at": {"type": "date"},
                    "period_start": {"type": "date"},
                    "period_end": {"type": "date"},
                    "analysis": {"type": "object", "enabled": False},
                }
            },
        )

    def save(self, document: dict[str, Any]) -> dict[str, Any]:
        self.create_index_if_missing()
        created_at = datetime.now(timezone.utc)
        response = self.client.index(
            index=self.index,
            document={**document, "created_at": created_at},
            refresh="wait_for",
        )
        return {"id": response["_id"], **document, "created_at": created_at}

    def list_for_email(self, email: str, limit: int = 20) -> list[dict[str, Any]]:
        self.create_index_if_missing()
        response = self.client.search(
            index=self.index,
            size=limit,
            query={"term": {"requested_by": email}},
            sort=[{"created_at": "desc"}],
        )
        return [
            {"id": hit["_id"], **hit["_source"]}
            for hit in response["hits"]["hits"]
        ]
