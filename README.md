# User Service

## Requirements

- Java 17
- Docker
- Docker Compose

## Building and Running the Application

1. Build and run the application using the provided shell script:
   ### Run in Local
    - Install prerequisites.
    - Run MailHog_windows_amd64.exe for fake smtp email.
   - Change 'spring.mail.host' value in property file to 'localhost'
    - Run the application by running LimboadduserApplication.java
    - Or use gradle script:
   ```sh
   ./gradlew bootRun
   ```

   ### Run in Docker
    - Install prerequisites.
    - Open Docker
    - Change 'spring.mail.host' value in property file to 'mailhog'
    - Run the script to start the app in Docker:
   ```sh
   ./run.sh
   ```

2. The application will be available at `http://localhost:8080`

## Swagger Documentation

- swagger : `http://localhost:8080/swagger-ui.html`.

## Endpoints

- `POST /api/users` - Register a new user
- `GET /api/users/{id}` - Get a user by ID
- `GET /api/users` - Get all users
- `PUT /api/asers/{id}` - Update a user by ID
- `DELETE /api/users/{id}` - Soft delete a user by ID
- `DELETE /api/users` - Soft delete a list of user by ID

## Testing

Run the tests with:

```sh
./gradlew test