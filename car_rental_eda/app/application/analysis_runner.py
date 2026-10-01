from datetime import datetime, timedelta, timezone

from app.domain.location_analysis import LocationAnalysisService
from app.application.ports import AnalysisStorePort, EventHistoryPort


class AnalysisRunService:
    def __init__(
        self,
        analysis: LocationAnalysisService,
        events: EventHistoryPort,
        repository: AnalysisStorePort,
        fallback_requester_email: str,
    ) -> None:
        self.analysis = analysis
        self.events = events
        self.repository = repository
        self.fallback_requester_email = fallback_requester_email

    def run(
        self,
        requested_by: str | None = None,
        period_start: datetime | None = None,
        period_end: datetime | None = None,
        trigger: str = "api",
    ) -> dict:
        end = period_end or datetime.now(timezone.utc)
        start = period_start or end - timedelta(days=30)
        if start.tzinfo is None:
            start = start.replace(tzinfo=timezone.utc)
        if end.tzinfo is None:
            end = end.replace(tzinfo=timezone.utc)
        if start >= end:
            raise ValueError("period_start must be earlier than period_end")

        period_events = self.events.find_between(start, end)
        state_events = self.events.find_as_of(end)
        result = self.analysis.analyze(period_events, state_events, start, end)
        result["trigger"] = trigger
        return self.repository.save({
            "requested_by": requested_by or self.fallback_requester_email,
            "period_start": start,
            "period_end": end,
            "analysis": result,
        })

    def list_for_email(self, email: str, limit: int = 20) -> list[dict]:
        return self.repository.list_for_email(email, limit)
