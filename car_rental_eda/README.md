# Car Rental Location EDA

FastAPI service that consumes location-related events from Kafka, stores the event history in Elasticsearch, and creates dated analysis snapshots on demand.

## Configuration

Set these environment variables in the service environment (or in a local `.env` file):

| Variable | Default | Purpose |
| --- | --- | --- |
| `ELASTICSEARCH_URL` | `http://localhost:9200` | Elasticsearch endpoint |
| `ELASTICSEARCH_USERNAME` | unset | Optional basic-auth username |
| `ELASTICSEARCH_PASSWORD` | unset | Optional basic-auth password |
| `ELASTICSEARCH_ANALYSIS_INDEX` | `car-rental-location-analyses` | Index for analysis snapshots |
| `ELASTICSEARCH_EVENTS_INDEX` | `car-rental-location-events` | Index for consumed Kafka events |
| `KAFKA_BOOTSTRAP_SERVERS` | `localhost:9092` | Kafka broker addresses |
| `KAFKA_LOCATION_EVENTS_TOPIC` | `car-rental.location-events` | Topic consumed by this service |
| `KAFKA_CONSUMER_GROUP` | `car-rental-location-eda` | Consumer group |
| `KAFKA_ENABLED` | `true` | Set to `false` to disable the consumer |
| `ANALYSIS_REQUESTED_BY` | `car-rental-eda@example.com` | Fallback email attached to automatic runs |
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
- `POST /api/analyses` calculates and stores an analysis snapshot with `requested_by` email and server-generated `created_at`. With no period supplied, it analyzes the last 30 days.
- `GET /api/analyses?email=...&limit=20` returns that requester's snapshots newest first.

POST body example:

```json
{
  "requested_by": "analyst@example.com",
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

The Spring Boot producer in `car_rental` publishes this contract. Configure it with `KAFKA_BOOTSTRAP_SERVERS` (default `localhost:9092`) and `KAFKA_LOCATION_EVENTS_TOPIC` (default `car-rental.location-events`).

Allowed `entity_type` values: `location`, `client`, `supplier`, `repairer`, `address`, and `buying`. Use `previous_location_id` and `new_location_id` for a real relocation. Initial creation events should omit `previous_location_id`. `event_id` must be stable and unique so Kafka redelivery is idempotent in Elasticsearch. Events without location changes can still include `location_id` and contribute to activity and destination counts.

The service runs a snapshot once during startup against event history already in Elasticsearch, then reruns the full analysis after each valid Kafka event is indexed. Automatic runs use an event's optional `requested_by` email, or `ANALYSIS_REQUESTED_BY` when absent. API-triggered runs use the submitted requester email. Each snapshot records its trigger (`startup`, `kafka_event`, or `api`).

Each analysis reports users per location/country (clients, suppliers, and repairers counted once at their latest known location by the period end), suppliers per location/country, and distinct buying records per location/country in the selected period. It also reports event and movement counts by entity type, busiest destinations, daily activity, and a 30-day flat daily-mean baseline. Buyings must carry a location or country in their Kafka event to be included in the respective breakdown. The baseline is withheld when fewer than seven events are found. It is a simple reference estimate, not a fitted forecasting model. Each query currently reads at most 10,000 events. The requester email is stored as submitted; API authentication and authorization are not implemented.
