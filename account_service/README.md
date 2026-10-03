# Account Service

This service is responsible for managing user accounts, including registration, login, and profile updates.

## Running the service

To run the service, you can use the following command:

```bash
./mvnw spring-boot:run
```

## API Endpoints

The following are the main API endpoints provided by this service:

*   `POST /api/v1/accounts/register`: Register a new user account.
*   `POST /api/v1/accounts/login`: Log in to an existing user account.
*   `PUT /api/v1/accounts/profile`: Update the profile of the currently logged-in user.
*   `PUT /api/v1/accounts/password`: Update the password of the currently logged-in user.