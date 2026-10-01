from collections import Counter
from datetime import date, datetime, timedelta, timezone
from typing import Any

class LocationAnalysisService:
    def analyze(
        self,
        events: list[dict[str, Any]],
        state_events: list[dict[str, Any]],
        period_start: datetime,
        period_end: datetime,
    ) -> dict[str, Any]:
        if period_start.tzinfo is None:
            period_start = period_start.replace(tzinfo=timezone.utc)
        if period_end.tzinfo is None:
            period_end = period_end.replace(tzinfo=timezone.utc)
        movements = [event for event in events if event.get("previous_location_id") is not None
                     and event.get("previous_location_id") != event.get("new_location_id")
                     and event.get("new_location_id") is not None]

        entity_counts = Counter(event.get("entity_type", "unknown") for event in events)
        movements_by_entity = Counter(event.get("entity_type", "unknown") for event in movements)
        destinations: Counter[str] = Counter()
        countries: dict[str, str] = {}
        daily_counts: Counter[date] = Counter()
        for event in events:
            destination = event.get("new_location_id") or event.get("location_id")
            if destination is not None:
                destinations[str(destination)] += 1
                country = event.get("country")
                if country:
                    countries[str(destination)] = country
            if event.get("occurred_at"):
                timestamp = datetime.fromisoformat(event["occurred_at"].replace("Z", "+00:00"))
                daily_counts[timestamp.date()] += 1

        span_days = max(1, (period_end.date() - period_start.date()).days + 1)
        daily_series = [
            {"date": (period_start.date() + timedelta(days=offset)).isoformat(),
             "event_count": daily_counts[period_start.date() + timedelta(days=offset)]}
            for offset in range(span_days)
        ]
        daily_mean = len(events) / span_days
        forecast = {
            "method": "flat daily mean",
            "horizon_days": 30,
            "expected_events": round(daily_mean * 30, 2) if len(events) >= 7 else None,
            "status": "available" if len(events) >= 7 else "insufficient_history",
            "minimum_events": 7,
        }
        latest_by_entity: dict[tuple[str, str], dict[str, Any]] = {}
        for event in state_events:
            entity_type = event.get("entity_type")
            entity_id = event.get("entity_id")
            if entity_type and entity_id:
                entity_key = (entity_type, str(entity_id))
                current = latest_by_entity.get(entity_key)
                if current is None or self._is_newer(event, current):
                    latest_by_entity[entity_key] = event

        location_country: dict[str, str] = {}
        for event in state_events:
            location_id = event.get("new_location_id") or event.get("location_id")
            if location_id is not None and event.get("country"):
                location_country.setdefault(str(location_id), event["country"])

        users_by_location: Counter[str] = Counter()
        users_by_country: Counter[str] = Counter()
        suppliers_by_location: Counter[str] = Counter()
        suppliers_by_country: Counter[str] = Counter()
        for (entity_type, _), event in latest_by_entity.items():
            if entity_type not in {"client", "supplier", "repairer"}:
                continue
            location_id = event.get("new_location_id") or event.get("location_id")
            if location_id is None:
                continue
            location_id = str(location_id)
            country = event.get("country") or location_country.get(location_id)
            users_by_location[location_id] += 1
            if country:
                users_by_country[country] += 1
            if entity_type == "supplier":
                suppliers_by_location[location_id] += 1
                if country:
                    suppliers_by_country[country] += 1

        buying_by_id: dict[str, dict[str, Any]] = {}
        for event in events:
            if event.get("entity_type") != "buying":
                continue
            buying_id = str(event.get("entity_id", event.get("event_id", "")))
            current = buying_by_id.get(buying_id)
            if current is None or self._is_newer(event, current):
                buying_by_id[buying_id] = event
        buyings_by_location: Counter[str] = Counter()
        buyings_by_country: Counter[str] = Counter()
        for event in buying_by_id.values():
            location_id = event.get("new_location_id") or event.get("location_id")
            country = event.get("country")
            if location_id is not None:
                location_id = str(location_id)
                buyings_by_location[location_id] += 1
                country = country or location_country.get(location_id)
            if country:
                buyings_by_country[country] += 1

        return {
            "event_count": len(events),
            "movement_count": len(movements),
            "population_as_of": period_end.isoformat(),
            "users_per_location": dict(users_by_location),
            "users_per_country": dict(users_by_country),
            "suppliers_per_location": dict(suppliers_by_location),
            "suppliers_per_country": dict(suppliers_by_country),
            "buyings_per_location": dict(buyings_by_location),
            "buyings_per_country": dict(buyings_by_country),
            "entity_event_counts": dict(entity_counts),
            "movement_counts_by_entity": dict(movements_by_entity),
            "top_destinations": [
                {"location_id": location_id, "country": countries.get(location_id), "event_count": count}
                for location_id, count in destinations.most_common(10)
            ],
            "daily_activity": daily_series,
            "forecast": forecast,
            "limitations": [
                "Forecast is a flat-rate baseline, not a fitted predictive model.",
                "At most 10000 events are included in one analysis run.",
                "Movement is counted only when previous_location_id and new_location_id differ.",
            ],
        }

    @staticmethod
    def _is_newer(candidate: dict[str, Any], current: dict[str, Any]) -> bool:
        def parse(value: str) -> datetime:
            timestamp = datetime.fromisoformat(value.replace("Z", "+00:00"))
            return timestamp.replace(tzinfo=timezone.utc) if timestamp.tzinfo is None else timestamp

        return parse(candidate["occurred_at"]) > parse(current["occurred_at"])
