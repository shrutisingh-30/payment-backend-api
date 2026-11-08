# Payment Backend API

A RESTful Payment Management API built using **Java, Spring Boot, Spring Data JPA, and PostgreSQL**.

This project demonstrates backend development concepts such as REST APIs, CRUD operations, input validation, exception handling, database integration, transaction management, and automated testing.

## Features

- Create and manage payment records.
- Retrieve all payments or a payment by ID.
- Update and delete payment records.
- Track payment status: PENDING, SUCCESS, and FAILED.
- Validate incoming payment requests.
- Handle exceptions with appropriate HTTP status codes.
- Persist payment data in PostgreSQL.
- Prevent changes to completed payment statuses.
- Automated testing using JUnit, Mockito, and Spring Boot Test.

## Tech Stack

| Technology | Purpose |
|---|---|
| Java | Programming language |
| Spring Boot | Backend framework |
| Spring Web MVC | REST API development |
| Spring Data JPA | Database operations |
| PostgreSQL | Relational database |
| Hibernate | ORM |
| Maven | Dependency management |
| JUnit and Mockito | Testing |

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| GET | `/hello` | Test the application |
| POST | `/payments` | Create a payment |
| GET | `/payments` | Retrieve all payments |
| GET | `/payments/{id}` | Retrieve payment by ID |
| PUT | `/payments/{id}` | Update payment details |
| DELETE | `/payments/{id}` | Delete a payment |
| PATCH | `/payments/{id}/status?status=SUCCESS` | Update payment status |

## Sample Request

**POST `/payments`**

```json
{
  "customerId": 101,
  "amount": 500.50,
  "paymentMethod": "UPI"
}
```

A newly created payment is assigned the initial status `PENDING`.

## Database Configuration

The application uses PostgreSQL.

Create a database named:

`payment_db`

Configure these environment variables before running the application:

- `DB_URL` – PostgreSQL database URL
- `DB_USERNAME` – PostgreSQL username
- `DB_PASSWORD` – PostgreSQL password

The default database URL and username are configured in `application.properties`. The password is supplied through an environment variable and should never be committed to GitHub.

## Run Locally

1. Clone the repository:

```bash
git clone https://github.com/shrutisingh-30/payment-backend-api.git
```

2. Open the project directory:

```bash
cd payment-backend-api
```

3. Set the database environment variables.

4. Run the application:

```bash
./mvnw spring-boot:run
```

On Windows Command Prompt, use:

```cmd
mvnw.cmd spring-boot:run
```

The application runs at:

`http://localhost:8080`

## Run Tests

On Windows:

```cmd
mvnw.cmd clean test
```

The test suite covers service logic, REST controller behavior, validation, exception handling, and application startup.

## Project Scope

This project is a **payment record management and simulation backend**. It demonstrates payment-related backend workflows but does not connect to an actual payment gateway or process real monetary transactions.

## API Documentation (Swagger)

This project uses Swagger UI for interactive API documentation and testing.

After starting the application, open:

- Swagger UI: http://localhost:8080/swagger-ui/index.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

You can view all available endpoints, provide request data, and test API operations directly from Swagger UI.

## Author

**Shruti Singh**

GitHub: https://github.com/shrutisingh-30