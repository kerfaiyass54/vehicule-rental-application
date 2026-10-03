# Budget Service

This service is responsible for tracking and managing user budgets. It provides an API for retrieving budget history and consumes Kafka events to update budget information.

## Running the service

To run the service, you can use the following command:

```bash
uvicorn app.main:app --host 0.0.0.0 --port 8062 --reload
```

## API Endpoints

The following are the main API endpoints provided by this service:

*   `GET /budget/history`: Retrieve the budget history for a user.

## Kafka Consumer

This service consumes messages from the `budget_events` Kafka topic. The messages should be in the following format:

```json
{
  "email": "user@example.com",
  "budget": 100.00,
  "date": "2023-10-27T10:00:00Z",
  "event_type": "reduction"
}
```