# Vehicle Sender

This service uses a local normalized subset of Kaggle's **Car Features and MSRP**
dataset as its semantic reference data. The full dataset can be obtained from:
https://www.kaggle.com/datasets/CooperUnion/cardataset

The request pipeline extracts constraints, applies TF-IDF semantic similarity,
then reranks by budget and vehicle attributes. It returns fields compatible with
the application's `Vehicule` entity.

## Running the service

To run the service, you can use the following command:

```bash
python app.py
```

## Kafka Producer

This service produces messages to the `vehicle_data` Kafka topic. The messages are in the following format:

```json
{
  "make": "BMW",
  "model": "1 Series",
  "year": 2011,
  "engine_fuel_type": "premium unleaded (required)",
  "engine_hp": 300,
  "engine_cylinders": 6,
  "transmission_type": "MANUAL",
  "driven_wheels": "rear wheel drive",
  "number_of_doors": 2,
  "market_category": "Factory Tuner,Luxury,High-Performance",
  "vehicle_size": "Compact",
  "vehicle_style": "Coupe",
  "highway_mpg": 28,
  "city_mpg": 20,
  "popularity": 3916,
  "msrp": 46135
}
```