# Car Rental EDA Service

This service is responsible for performing event-driven analysis of the car rental data. It consumes Kafka events and stores the data in Elasticsearch for analysis.

## Running the service

To run the service, you can use the following command:

```bash
uvicorn app.main:app --host 0.0.0.0 --port 8063 --reload
```

## Kafka Consumer

This service consumes messages from the `car_rental_events` Kafka topic. The messages should be in the following format:

```json
{
  "event_type": "rental",
  "vehicule_id": "12345",
  "client_id": "67890",
  "start_date": "2023-10-27T10:00:00Z",
  "end_date": "2023-10-28T10:00:00Z"
}
```