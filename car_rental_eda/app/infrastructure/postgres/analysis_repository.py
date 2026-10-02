from datetime import datetime
from datetime import timezone
from typing import Any

import psycopg
from psycopg.rows import dict_row

from app.infrastructure.config.settings import get_settings


class PostgresAnalysisRepository:
    """Reads current rental data and all dated transactions from PostgreSQL."""

    def __init__(self) -> None:
        self.settings = get_settings()

    def load_analysis_data(
        self, period_start: datetime, period_end: datetime
    ) -> tuple[list[dict[str, Any]], list[dict[str, Any]]]:
        state_events: list[dict[str, Any]] = []
        transactions: list[dict[str, Any]] = []
        population_as_of = datetime.now(timezone.utc)
        with psycopg.connect(
            host=self.settings.postgres_host,
            port=self.settings.postgres_port,
            dbname=self.settings.postgres_database,
            user=self.settings.postgres_username,
            password=self.settings.postgres_password,
            row_factory=dict_row,
        ) as connection:
            with connection.cursor() as cursor:
                cursor.execute("""
                    SELECT id_location, location_name, country FROM locations
                """)
                for row in cursor.fetchall():
                    state_events.append(self._state_event(
                        "location", row["id_location"], row["id_location"], row, population_as_of
                    ))

                cursor.execute("""
                    SELECT c.id_client AS entity_id, c.id_location AS location_id,
                           l.location_name, l.country
                    FROM clients c JOIN locations l ON l.id_location = c.id_location
                """)
                state_events.extend(self._rows_as_state(cursor.fetchall(), "client", population_as_of))

                cursor.execute("""
                    SELECT s.id_supplier AS entity_id, l.id_location AS location_id,
                           l.location_name, l.country
                    FROM suppliers s
                    LEFT JOIN LATERAL (
                        SELECT a.id_location FROM addresses a
                        WHERE a.id_supplier = s.id_supplier AND a.address_status = 'ASSIGNED'
                        ORDER BY a.id_address DESC LIMIT 1
                    ) assigned ON TRUE
                    LEFT JOIN locations l ON l.id_location = assigned.id_location
                """)
                state_events.extend(self._rows_as_state(cursor.fetchall(), "supplier", population_as_of))

                cursor.execute("""
                    SELECT r.id_repair AS entity_id, r.id_location AS location_id,
                           l.location_name, l.country
                    FROM repairs r JOIN locations l ON l.id_location = r.id_location
                """)
                state_events.extend(self._rows_as_state(cursor.fetchall(), "repairer", population_as_of))

                cursor.execute("""
                    SELECT b.id_buying AS entity_id, b.date_buy AS occurred_at,
                           c.id_location AS previous_location_id,
                           COALESCE(destination.id_location, c.id_location) AS new_location_id,
                           COALESCE(destination.location_name, client_location.location_name) AS location_name,
                           COALESCE(destination.country, client_location.country) AS country
                    FROM buyings b
                    JOIN clients c ON c.id_client = b.id_client
                    JOIN locations client_location ON client_location.id_location = c.id_location
                    LEFT JOIN LATERAL (
                        SELECT l.id_location, l.location_name, l.country
                        FROM addresses a JOIN locations l ON l.id_location = a.id_location
                        WHERE a.id_supplier = b.id_supplier AND a.address_status = 'ASSIGNED'
                        ORDER BY a.id_address DESC LIMIT 1
                    ) destination ON TRUE
                """)
                transactions.extend(self._rows_as_transactions(cursor.fetchall(), "buying"))

                cursor.execute("""
                    SELECT t.id_ticket AS entity_id, t.date_insert AS occurred_at,
                           c.id_location AS previous_location_id,
                           r.id_location AS new_location_id,
                           l.location_name, l.country
                    FROM tickets t
                    JOIN clients c ON c.id_client = t.id_client
                    JOIN repairs r ON r.id_repair = t.id_repair
                    JOIN locations l ON l.id_location = r.id_location
                """)
                transactions.extend(self._rows_as_transactions(cursor.fetchall(), "ticket"))

        return state_events, transactions

    @staticmethod
    def _state_event(
        entity_type: str, entity_id: Any, location_id: Any,
        row: dict[str, Any], as_of: datetime,
    ) -> dict[str, Any]:
        return {
            "event_id": f"database:{entity_type}:{entity_id}",
            "event_type": "database_state",
            "entity_type": entity_type,
            "entity_id": str(entity_id),
            "occurred_at": as_of.isoformat(),
            "location_id": str(location_id) if location_id is not None else None,
            "new_location_id": str(location_id) if location_id is not None else None,
            "location_name": row.get("location_name"),
            "country": row.get("country"),
        }

    @classmethod
    def _rows_as_state(
        cls, rows: list[dict[str, Any]], entity_type: str, as_of: datetime
    ) -> list[dict[str, Any]]:
        return [cls._state_event(entity_type, row["entity_id"], row.get("location_id"), row, as_of)
                for row in rows]

    @staticmethod
    def _rows_as_transactions(
        rows: list[dict[str, Any]], entity_type: str
    ) -> list[dict[str, Any]]:
        result = []
        for row in rows:
            result.append({
                "event_id": f"database:{entity_type}:{row['entity_id']}",
                "event_type": "database_transaction",
                "entity_type": entity_type,
                "entity_id": str(row["entity_id"]),
                "occurred_at": row["occurred_at"].isoformat(),
                "previous_location_id": str(row["previous_location_id"])
                    if row["previous_location_id"] is not None else None,
                "new_location_id": str(row["new_location_id"])
                    if row["new_location_id"] is not None else None,
                "location_id": str(row["new_location_id"])
                    if row["new_location_id"] is not None else None,
                "location_name": row.get("location_name"),
                "country": row.get("country"),
            })
        return result
