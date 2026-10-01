# Car Rental Location EDA

FastAPI service that consumes location-related events from Kafka, stores the event history in Elasticsearch, and creates dated analysis snapshots on demand.

## Configuration

Settings load the repository-level `.env` and then `car_rental_eda/.env` (which takes precedence); process environment variables take precedence over both. The Elasticsearch password accepts either `ELASTICSEARCH_PASSWORD` or the Docker Compose variable `ELASTIC_PASSWORD`.

| Variable | Default | Purpose |
| --- | --- | --- |
| `ELASTICSEARCH_URL` | `http://localhost:9200` | Elasticsearch endpoint |
| `ELASTICSEARCH_USERNAME` | `elastic` | Elasticsearch basic-auth username |
| `ELASTICSEARCH_PASSWORD` / `ELASTIC_PASSWORD` | loaded from repository `.env` when available | Elasticsearch basic-auth password; a service `.env` can override it |
| `ELASTICSEARCH_ANALYSIS_INDEX` | `car-rental-location-analyses` | Index for analysis snapshots |
| `ELASTICSEARCH_EVENTS_INDEX` | `car-rental-location-events` | Index for consumed Kafka events |
| `KAFKA_BOOTSTRAP_SERVERS` | `localhost:9194` | Kafka broker addresses for local host processes |
| `KAFKA_LOCATION_EVENTS_TOPIC` | `car-rental.location-events` | Topic consumed by this service |
| `KAFKA_CONSUMER_GROUP` | `car-rental-location-eda` | Consumer group |
| `KAFKA_ENABLED` | `true` | Set to `false` to disable the consumer |
| `API_HOST` | `0.0.0.0` | API bind address |
| `API_PORT` | `8060` | API port |

## Run

From this directory, install `requirements.txt`, then run:

```shell
uvicorn app.main:app --host 0.0.0.0 --port 8060
```

## Project structure

```text
app/
  api/             HTTP routes and request/response schemas
  application/     analysis orchestration and snapshot use case
                   ports for event-history and analysis storage
  domain/          location analysis calculations
  infrastructure/
    config/        environment-backed settings
    elasticsearch/ event and analysis persistence adapters
    kafka/         event contract and Kafka consumer
  main.py          dependency wiring and application lifecycle
```

## API

- `GET /health` checks that the API process is running.
- `POST /api/analyses?email=analyst@example.com` calculates and stores an analysis snapshot with the frontend-supplied email and server-generated `created_at`. With no period supplied, it analyzes the last 30 days.
- `GET /api/analyses?email=...&limit=20` returns that requester's snapshots newest first.

POST body example:

```json
{
  "period_start": "2026-01-01T00:00:00Z",
  "period_end": "2026-02-01T00:00:00Z"
}
```

## Kafka event contract

Publish JSON events to `car-rental.location-events` (or the configured topic). Example:

```json
{
  "event_id": "5efb3d28-3095-405f-941e-4b87971ee314",
  "event_type": "client.location_changed",
  "entity_type": "client",
  "entity_id": "42",
  "occurred_at": "2026-01-15T12:30:00Z",
  "previous_location_id": "7",
  "new_location_id": "12",
  "location_name": "Lyon Center",
  "country": "France",
  "latitude": 45.764,
  "longitude": 4.8357
}
```

The Spring Boot producer in `car_rental` publishes this contract. Configure both services with the same `KAFKA_BOOTSTRAP_SERVERS` and `KAFKA_LOCATION_EVENTS_TOPIC` (default topic `car-rental.location-events`). The EDA consumer creates the topic on startup if it is missing; the local single-broker setup uses one partition and replication factor one. For applications running in Docker Compose, use `vehicule-kafka:9092`; host processes use `localhost:9194`. Compose advertises these as separate internal and external listeners.

Allowed `entity_type` values: `location`, `client`, `supplier`, `repairer`, `address`, and `buying`. Use `previous_location_id` and `new_location_id` for a real relocation. Initial creation events should omit `previous_location_id`. `event_id` must be stable and unique so Kafka redelivery is idempotent in Elasticsearch. Events without location changes can still include `location_id` and contribute to activity and destination counts.

The service runs a snapshot once during startup against event history already in Elasticsearch, then reruns the full analysis after each valid Kafka event is indexed. Only API-triggered runs have an email, supplied by the frontend as the `email` query parameter. Automatic `startup` and `kafka_event` snapshots leave `requested_by` empty. No email is derived from authentication or Kafka event data.

Each analysis reports users per location/country (clients, suppliers, and repairers counted once at their latest known location by the period end), suppliers per location/country, and distinct buying records per location/country in the selected period. It also reports event and movement counts by entity type, busiest destinations, daily activity, and a 30-day flat daily-mean baseline. Buyings must carry a location or country in their Kafka event to be included in the respective breakdown. The baseline is withheld when fewer than seven events are found. It is a simple reference estimate, not a fitted forecasting model. Each query currently reads at most 10,000 events.
