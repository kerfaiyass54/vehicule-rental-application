from datetime import datetime
from typing import Any, Protocol


class EventHistoryPort(Protocol):
    def find_between(self, start: datetime, end: datetime) -> list[dict[str, Any]]: ...


class DatabaseAnalysisPort(Protocol):
    def load_analysis_data(
        self, period_start: datetime, period_end: datetime
    ) -> tuple[list[dict[str, Any]], list[dict[str, Any]]]: ...


class AnalysisStorePort(Protocol):
    def save(self, document: dict[str, Any]) -> dict[str, Any]: ...

    def list_for_email(self, email: str, limit: int = 20) -> list[dict[str, Any]]: ...
