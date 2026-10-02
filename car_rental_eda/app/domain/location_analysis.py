from collections import Counter
from datetime import date, datetime, timedelta, timezone
from typing import Any


class LocationAnalysisService:
    """Builds location populations, transaction flows, movements, and activity metrics."""

    def analyze(
        self,
        events: list[dict[str, Any]],
        state_events: list[dict[str, Any]],
        transaction_events: list[dict[str, Any]],
        period_start: datetime,
        period_end: datetime,
    ) -> dict[str, Any]:
        if period_start.tzinfo is None:
            period_start = period_start.replace(tzinfo=timezone.utc)
        if period_end.tzinfo is None:
            period_end = period_end.replace(tzinfo=timezone.utc)

        # PostgreSQL supplies the current populations and dated transactions.
        # Kafka supplies only the event history used for movements and activity.
        live_events = [event for event in events if event.get("entity_type") not in {"buying", "ticket"}]
        buying_records = [event for event in transaction_events if event.get("entity_type") == "buying"]
        ticket_records = [event for event in transaction_events if event.get("entity_type") == "ticket"]
        period_buyings = self._within_period(buying_records, period_start, period_end)
        period_tickets = self._within_period(ticket_records, period_start, period_end)
        metric_events = live_events + period_buyings + period_tickets

        movements = [
            event for event in live_events
            if event.get("entity_type") in {"location", "client", "supplier", "repairer", "address"}
            and event.get("previous_location_id") is not None
            and event.get("previous_location_id") != event.get("new_location_id")
            and event.get("new_location_id") is not None
        ]
        entity_counts = Counter(event.get("entity_type", "unknown") for event in metric_events)
        movement_counts = Counter(event.get("entity_type", "unknown") for event in movements)
        daily_counts: Counter[date] = Counter()
        destinations: Counter[str] = Counter()
        for event in metric_events:
            location_id = event.get("new_location_id") or event.get("location_id")
            if location_id is not None:
                destinations[str(location_id)] += 1
            timestamp = self._timestamp(event)
            if timestamp:
                daily_counts[timestamp.date()] += 1

        span_days = max(1, (period_end.date() - period_start.date()).days + 1)
        daily_activity = [
            {"date": (period_start.date() + timedelta(days=day)).isoformat(),
             "event_count": daily_counts[period_start.date() + timedelta(days=day)]}
            for day in range(span_days)
        ]
        event_count = len(metric_events)
        forecast = {
            "method": "flat daily mean",
            "horizon_days": 30,
            "expected_events": round(event_count / span_days * 30, 2) if event_count >= 7 else None,
            "status": "available" if event_count >= 7 else "insufficient_history",
            "minimum_events": 7,
        }

        latest_entities: dict[tuple[str, str], dict[str, Any]] = {}
        location_names: dict[str, str] = {}
        location_countries: dict[str, str] = {}
        for event in state_events:
            entity_type, entity_id = event.get("entity_type"), event.get("entity_id")
            if entity_type and entity_id:
                key = (str(entity_type), str(entity_id))
                current = latest_entities.get(key)
                if current is None or self._is_newer(event, current):
                    latest_entities[key] = event
            location_id = event.get("new_location_id") or event.get("location_id")
            if location_id is not None:
                location_id = str(location_id)
                if event.get("country"):
                    location_countries.setdefault(location_id, str(event["country"]))
                if event.get("location_name"):
                    location_names.setdefault(location_id, str(event["location_name"]))
            if entity_type == "location" and entity_id:
                location_id = str(entity_id)
                location_names[location_id] = str(event.get("location_name") or location_id)
                if event.get("country"):
                    location_countries[location_id] = str(event["country"])

        counts_by_entity: dict[str, Counter[str]] = {
            "client": Counter(), "supplier": Counter(), "repairer": Counter(),
        }
        for (entity_type, _), event in latest_entities.items():
            if entity_type in counts_by_entity:
                location_id = event.get("new_location_id") or event.get("location_id")
                if location_id is not None:
                    counts_by_entity[entity_type][str(location_id)] += 1

        clients = counts_by_entity["client"]
        suppliers = counts_by_entity["supplier"]
        repairs = counts_by_entity["repairer"]
        users: Counter[str] = Counter()
        # In this domain, users are the rental clients. Suppliers and repairers
        # are reported as their own populations below.
        users.update(clients)
        clients_country = self._by_country(clients, location_countries)
        suppliers_country = self._by_country(suppliers, location_countries)
        repairs_country = self._by_country(repairs, location_countries)
        users_country = self._by_country(users, location_countries)

        buyings, buyings_country, buying_routes = self._transaction_counts(buying_records, location_countries)
        tickets, tickets_country, ticket_routes = self._transaction_counts(ticket_records, location_countries)
        period_buyings_by_location, period_buyings_by_country, period_buying_routes = self._transaction_counts(
            period_buyings, location_countries
        )
        period_tickets_by_location, period_tickets_by_country, period_ticket_routes = self._transaction_counts(
            period_tickets, location_countries
        )
        buying_in, buying_out = self._route_totals(buying_routes)
        ticket_in, ticket_out = self._route_totals(ticket_routes)
        period_buying_in, period_buying_out = self._route_totals(period_buying_routes)
        period_ticket_in, period_ticket_out = self._route_totals(period_ticket_routes)
        movement_in: Counter[str] = Counter()
        movement_out: Counter[str] = Counter()
        for event in movements:
            if event.get("previous_location_id") is not None:
                movement_out[str(event["previous_location_id"])] += 1
            if event.get("new_location_id") is not None:
                movement_in[str(event["new_location_id"])] += 1

        all_locations = (
            set(location_names) | set(location_countries) | set(users) |
            set(buyings) | set(tickets)
        )
        location_stats = [
            {
                "location_id": location_id,
                "location_name": location_names.get(location_id, location_id),
                "country": location_countries.get(location_id),
                "users": users[location_id],
                "clients": clients[location_id],
                "suppliers": suppliers[location_id],
                "repairs": repairs[location_id],
                "buyings": buyings[location_id],
                "buyings_in_period": period_buyings_by_location[location_id],
                "tickets": tickets[location_id],
                "tickets_in_period": period_tickets_by_location[location_id],
                "buyings_in": buying_in[location_id],
                "buyings_out": buying_out[location_id],
                "buyings_in_period_in": period_buying_in[location_id],
                "buyings_in_period_out": period_buying_out[location_id],
                "tickets_in": ticket_in[location_id],
                "tickets_out": ticket_out[location_id],
                "tickets_in_period_in": period_ticket_in[location_id],
                "tickets_in_period_out": period_ticket_out[location_id],
                "movements_in": movement_in[location_id],
                "movements_out": movement_out[location_id],
            }
            for location_id in sorted(all_locations)
        ]

        return {
            "event_count": event_count,
            "movement_count": len(movements),
            "population_as_of": datetime.now(timezone.utc).isoformat(),
            "location_stats": location_stats,
            "users_per_location": dict(users),
            "users_per_country": dict(users_country),
            "clients_per_location": dict(clients),
            "clients_per_country": dict(clients_country),
            "repairs_per_location": dict(repairs),
            "repairs_per_country": dict(repairs_country),
            "suppliers_per_location": dict(suppliers),
            "suppliers_per_country": dict(suppliers_country),
            "buyings_per_location": dict(buyings),
            "buyings_per_country": dict(buyings_country),
            "buyings_between_locations": buying_routes,
            "buyings_in_period": len(period_buyings),
            "buyings_per_location_in_period": dict(period_buyings_by_location),
            "buyings_per_country_in_period": dict(period_buyings_by_country),
            "buyings_between_locations_in_period": period_buying_routes,
            "ticket_count": len(ticket_records),
            "tickets_per_location": dict(tickets),
            "tickets_per_country": dict(tickets_country),
            "tickets_between_locations": ticket_routes,
            "tickets_in_period": len(period_tickets),
            "tickets_per_location_in_period": dict(period_tickets_by_location),
            "tickets_per_country_in_period": dict(period_tickets_by_country),
            "tickets_between_locations_in_period": period_ticket_routes,
            "entity_event_counts": dict(entity_counts),
            "movement_counts_by_entity": dict(movement_counts),
            "top_destinations": [
                {"location_id": key, "location_name": location_names.get(key),
                 "country": location_countries.get(key), "event_count": count}
                for key, count in destinations.most_common(10)
            ],
            "daily_activity": daily_activity,
            "forecast": forecast,
            "limitations": [
                "Populations are read live from PostgreSQL when the analysis runs; historical populations are not reconstructed.",
                "Movement history begins when location events are published to Kafka.",
                "Forecast is a flat-rate baseline, not a fitted predictive model.",
                "At most 10000 events are included in one analysis run.",
                "Movement is counted only when previous_location_id and new_location_id differ.",
            ],
        }

    @staticmethod
    def _transaction_counts(
        transactions: list[dict[str, Any]], countries: dict[str, str]
    ) -> tuple[Counter[str], Counter[str], list[dict[str, Any]]]:
        by_location: Counter[str] = Counter()
        by_country: Counter[str] = Counter()
        route_counts: Counter[tuple[str, str]] = Counter()
        for event in transactions:
            destination = event.get("new_location_id") or event.get("location_id")
            origin = event.get("previous_location_id")
            country = event.get("country")
            if destination is not None:
                destination = str(destination)
                by_location[destination] += 1
                country = country or countries.get(destination)
                if origin is not None and str(origin) != destination:
                    route_counts[(str(origin), destination)] += 1
            if country:
                by_country[str(country)] += 1
        routes = [
            {"from_location_id": source, "to_location_id": target, "count": count}
            for (source, target), count in sorted(route_counts.items())
        ]
        return by_location, by_country, routes

    @classmethod
    def _within_period(
        cls, transactions: list[dict[str, Any]], start: datetime, end: datetime
    ) -> list[dict[str, Any]]:
        return [
            event for event in transactions
            if (timestamp := cls._timestamp(event)) is not None
            and start <= timestamp <= end
        ]

    @staticmethod
    def _route_totals(routes: list[dict[str, Any]]) -> tuple[Counter[str], Counter[str]]:
        inbound: Counter[str] = Counter()
        outbound: Counter[str] = Counter()
        for route in routes:
            outbound[str(route["from_location_id"])] += route["count"]
            inbound[str(route["to_location_id"])] += route["count"]
        return inbound, outbound

    @staticmethod
    def _by_country(counts: Counter[str], countries: dict[str, str]) -> Counter[str]:
        result: Counter[str] = Counter()
        for location_id, count in counts.items():
            if countries.get(location_id):
                result[countries[location_id]] += count
        return result

    @staticmethod
    def _timestamp(event: dict[str, Any]) -> datetime | None:
        value = event.get("occurred_at")
        if not value:
            return None
        timestamp = value if isinstance(value, datetime) else datetime.fromisoformat(str(value).replace("Z", "+00:00"))
        return timestamp.replace(tzinfo=timezone.utc) if timestamp.tzinfo is None else timestamp

    @classmethod
    def _is_newer(cls, candidate: dict[str, Any], current: dict[str, Any]) -> bool:
        candidate_time, current_time = cls._timestamp(candidate), cls._timestamp(current)
        return candidate_time is not None and (current_time is None or candidate_time > current_time)
