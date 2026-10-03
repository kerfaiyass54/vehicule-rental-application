# Vehicle repair estimator

This service estimates repair tasks and shop time from a ticket description.
It uses the public Kaggle dataset **My Car's Maintenance Diary: Real Service
Cost Data**, by `josephnehrenz`, version 4:

https://www.kaggle.com/datasets/josephnehrenz/my-cars-maintenance-diary-real-service-cost-data

The checked-in CSV files are the downloaded dataset used by the estimator.
Observed visit durations are derived from `request_opened` and `request_ready`,
then distributed across the line items for task-level estimates. The dataset is
small, so the returned estimate is a prototype estimate and not a replacement
for a repairer's inspection.

The `POST /repair-estimates` endpoint creates the
`vehicle-repair-estimates` Elasticsearch index with mappings when it does not
exist, stores every estimate, and returns the stored result.

## Running the service

To run the service, you can use the following command:

```bash
python app.py
```

## API Endpoints

The following is the main API endpoint provided by this service:

*   `POST /repair-estimates`: Estimate the cost of a repair.

```json
{
  "ticket_description": "The car is making a strange noise."
}
```

## Response Format

The following is an example of the response format:

```json
{
  "estimated_cost": 150.00,
  "estimated_time": "2 hours"
}
```