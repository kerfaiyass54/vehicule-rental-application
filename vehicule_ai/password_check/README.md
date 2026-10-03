# Password Check Service

This service is a machine learning model that checks the strength of a password. It provides an API to which you can send a password, and it will return a prediction of whether the password is weak, medium, or strong.

## Running the service

To run the service, you can use the following command:

```bash
python main.py
```

## API Endpoints

The following is the main API endpoint provided by this service:

*   `POST /predict`: Send a password to be checked.

```json
{
  "password": "mysecretpassword"
}
```