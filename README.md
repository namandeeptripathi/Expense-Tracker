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
export JWT_SECRET="$(openssl rand -base64 32)"
mvn spring-boot:run
```

The starter exposes health at `/actuator/health` and Swagger UI at `/swagger-ui/index.html`.

Flyway creates the authentication tables during startup. The API documentation is available at
`/swagger-ui/index.html`; authentication endpoints are under `/api/v1/auth`.

Authenticated expense management is available under `/api/v1/expenses`. Each expense is scoped to the
Bearer-token user and supports pagination, allow-listed sorting, title search, category/payment-method,
date-range, and amount-range filters.

Categories are managed at `/api/v1/categories`. They are private to the Bearer-token user; a category
with referenced expenses must supply `reassignToCategoryId` when deleted.
