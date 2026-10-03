# Suspicious Login Detection Service

This service is a machine learning model that detects suspicious login attempts. It provides an API to which you can send login data, and it will return a prediction of whether the login is suspicious or not.

## Running the service

To run the service, you can use the following command:

```bash
python suspicious.py
```

## API Endpoints

The following is the main API endpoint provided by this service:

*   `POST /predict`: Send login data to be checked.

```json
{
  "username": "testuser",
  "ip_address": "123.123.123.123",
  "user_agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36"
}
```