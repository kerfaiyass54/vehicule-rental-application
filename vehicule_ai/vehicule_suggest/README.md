# Vehicle Suggestion Service

This service is a machine learning model that suggests vehicles to users based on their preferences. It provides an API to which you can send user preferences, and it will return a list of suggested vehicles.

## Running the service

To run the service, you can use the following command:

```bash
python main.py
```

## API Endpoints

The following is the main API endpoint provided by this service:

*   `POST /suggest`: Send user preferences to get vehicle suggestions.

```json
{
  "budget": 50000,
  "vehicle_type": "SUV",
  "number_of_seats": 5
}
```