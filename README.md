# Expense Tracker

Spring Boot 3.5 / Java 21 foundation for the Expense Tracker API.

## Prerequisites

- Java 21
- Maven 3.9+
- MySQL 8+

## Run locally

Set the database environment variables, then start the service:

```bash
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=expense_tracker
export DB_USERNAME=expense_tracker
export DB_PASSWORD=change-me
mvn spring-boot:run
```

The starter exposes health at `/actuator/health` and Swagger UI at `/swagger-ui/index.html`.

The application intentionally contains no domain models, persistence mappings, endpoints, or business rules yet.
