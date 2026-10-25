# Equipment Borrowing Management System

A RESTful backend for managing equipment inventory, borrowing requests, approvals, returns, and maintenance records. Built with **Java 21**, **Spring Boot 4.1.0**, **Spring Security**, **Spring Data JPA**, and **PostgreSQL**.

> **Project status:** Backend development / portfolio project. Core borrowing and maintenance workflows, JWT authentication, role-based authorization, exception handling, and transactional updates have been implemented and manually exercised. Automated test coverage and deployment hardening are ongoing.

## Overview

The system helps an organization keep track of equipment and borrowing activities. Borrowers submit requests, administrators manage the borrowing lifecycle, and technicians record equipment inspections and outcomes. The API separates HTTP controllers, business services, persistence repositories, and DTOs.

## Technology stack

| Area | Technology |
| --- | --- |
| Language | Java 21 |
| Framework | Spring Boot 4.1.0 |
| API | Spring Web MVC / REST |
| Security | Spring Security, JWT (JJWT), BCrypt |
| Persistence | Spring Data JPA / Hibernate |
| Database | PostgreSQL |
| Validation | Jakarta Bean Validation |
| Documentation | springdoc-openapi / Swagger UI |
| Build | Maven / Maven Wrapper |
| Testing | JUnit 5 and Mockito (initial tests; broader suite planned) |

## Features

- **Authentication:** account signup, login, BCrypt password hashing, and JWT issuance and verification.
- **Authorization:** `ADMIN`, `BORROWER`, and `TECHNICIAN` roles; method-level restrictions and borrower ownership checks.
- **Equipment inventory:** equipment records, categories, conditions, and availability/status tracking.
- **Borrowing requests:** create, list, retrieve, update, approve, deny, cancel, mark borrowed, and mark returned.
- **Ownership protection:** a borrower is assigned from the authenticated principal rather than a client-provided borrower ID; borrowers can access their own requests while admins can manage broader workflows.
- **Maintenance:** create and manage inspection records; mark inspected equipment as `GOOD`, `BROKEN`, or `DAMAGED`.
- **Error handling:** validation errors (`400`), forbidden access (`403`), missing resources (`404`), and business-rule conflicts (`409`).
- **Database consistency:** `@Transactional` on multi-record operations; a rollback was verified manually in the maintenance workflow.
- **Interactive API documentation:** Swagger UI with JWT Bearer authorization.

## Roles and permissions

| Role | Intended responsibilities |
| --- | --- |
| `BORROWER` | Submit borrowing requests, view/update permitted own requests, cancel eligible own requests |
| `ADMIN` | Manage borrowing approvals, denials, issue/return workflows, and administrative records |
| `TECHNICIAN` | Record and complete equipment inspections |

Authorization is enforced by Spring Security and service-level ownership checks. **Review every controller's current annotations before deploying:** in particular, restrict user-management operations to admins and review general maintenance CRUD permissions. A successful authenticated call alone does not establish that every endpoint has the intended authorization policy.

## Borrowing lifecycle

```text
PENDING
  |-- approve --> APPROVED --> BORROWED --> RETURNED
  |-- deny ----> DENIED
  `-- cancel --> CANCELLED

APPROVED -- cancel --> CANCELLED
```

Equipment status transitions used by the service include `AVAILABLE`, `RESERVED`, `BORROWED`, and `MAINTENANCE`. Approval checks equipment availability before reserving items. The exact transitions and reversal behavior are determined by the current service implementation; verify the equipment state when cancelling or returning requests.

## Maintenance workflow

A maintenance record references equipment and a technician. Inspection outcomes currently map as follows:

| Outcome | Equipment condition | Equipment status |
| --- | --- | --- |
| `GOOD` | `GOOD` | `AVAILABLE` |
| `BROKEN` | `BROKEN` | `ARCHIVED` |
| `DAMAGED` | `DAMAGED` | `MAINTENANCE` |

The completion operations require a maintenance record in `UNDER_INSPECTION`. The maintenance create DTO should therefore use that condition when preparing a record for completion.

## Project structure

Representative package layout (the exact files may vary as development continues):

```text
src/
├── main/
│   ├── java/org/dean/borrower/
│   │   ├── Auth/              # Authentication service, JWT service/filter, auth DTOs
│   │   ├── config/            # Security and OpenAPI configuration
│   │   ├── controller/        # REST controllers
│   │   ├── dto/               # Request and response DTOs
│   │   ├── entity/            # JPA entities
│   │   ├── enums/             # Roles, statuses, conditions
│   │   ├── exception/         # Custom exceptions and global handler
│   │   ├── repository/        # Spring Data repositories
│   │   ├── service/           # Business logic and transactions
│   │   └── BorrowerApplication.java
│   └── resources/
└── test/java/org/dean/borrower/
    └── service/               # JUnit / Mockito service tests
```

## Prerequisites

- JDK **21**
- PostgreSQL
- Maven Wrapper (`mvnw`, included in the project)
- A database and credentials configured for the application

## Local setup

1. Clone the repository and enter its directory:

   ```bash
   git clone <YOUR_REPOSITORY_URL>
   cd Borrower
   ```

2. Create a local PostgreSQL database. Configure the application's datasource settings in `src/main/resources/application.properties` or `application.yml`. Use your **actual** database name and username:

   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/<DATABASE_NAME>
   spring.datasource.username=${DB_USERNAME}
   spring.datasource.password=${DB_PASSWORD}
   ```

   Configure the JWT signing secret according to the property read by your `JwtService`. **Do not commit real credentials or signing secrets.** Set them using environment variables or a local ignored configuration file. Ensure your database schema exists; automated Flyway migrations are planned, not yet documented as installed.

3. Build the project:

   ```bash
   ./mvnw clean compile
   ```

4. Start the API:

   ```bash
   ./mvnw spring-boot:run
   ```

5. By default, the application is available at `http://localhost:8080`.

> The exact datasource/JWT property names and schema initialization mode must be checked against the repository's current configuration. This README intentionally does not invent a JWT property key.

## API endpoints

The following are the endpoints discussed and exercised during development. **Swagger UI is the source of truth for the exact current controller mappings and DTO schemas.**

### Authentication

| Method | Path | Description |
| --- | --- | --- |
| POST | `/api/auth/signup` | Register a borrower account |
| POST | `/api/auth/login` | Authenticate and receive a JWT |

### Equipment and users

| Method | Path | Description |
| --- | --- | --- |
| GET | `/api/equipment` | List equipment |
| GET | `/api/equipment/{id}` | Retrieve equipment |
| GET | `/api/users` | List users (should be admin-restricted) |
| GET | `/api/users/{id}` | Retrieve user (review permissions) |

Other CRUD routes for users, equipment, and categories are exposed by their respective controllers; inspect Swagger for their current paths and restrictions.

### Borrow requests

| Method | Path | Description |
| --- | --- | --- |
| GET | `/api/borrow-requests` | List requests (ownership-aware) |
| GET | `/api/borrow-requests/{id}` | Retrieve a request |
| POST | `/api/borrow-requests` | Create a request |
| PUT | `/api/borrow-requests/{id}` | Update a request |
| DELETE | `/api/borrow-requests/{id}` | Delete a request (admin-restricted) |
| PUT | `/api/borrow-requests/{id}/approve` | Approve (admin) |
| PUT | `/api/borrow-requests/{id}/deny` | Deny (admin) |
| PUT | `/api/borrow-requests/{id}/cancel` | Cancel (admin or authorized borrower) |
| PUT | `/api/borrow-requests/{id}/borrow` | Mark borrowed (admin) |
| PUT | `/api/borrow-requests/{id}/return` | Mark returned (admin) |

### Maintenance records

| Method | Path | Description |
| --- | --- | --- |
| GET | `/api/maintenance_record` | List records |
| GET | `/api/maintenance_record/{id}` | Retrieve a record |
| POST | `/api/maintenance_record` | Create a record |
| PUT | `/api/maintenance_record/{id}` | Update a record |
| DELETE | `/api/maintenance_record/{id}` | Delete a record |
| PUT | `/api/maintenance_record/{id}/good` | Mark inspection GOOD (technician) |
| PUT | `/api/maintenance_record/{id}/broken` | Mark inspection BROKEN (technician) |
| PUT | `/api/maintenance_record/{id}/damaged` | Mark inspection DAMAGED (technician) |

## Authentication examples

Create a borrower account:

```bash
curl -i -X POST http://localhost:8080/api/auth/signup \
  -H 'Content-Type: application/json' \
  -d '{
    "firstName": "Test",
    "lastName": "Borrower",
    "email": "borrower.test@example.com",
    "password": "<CHOOSE_A_PASSWORD>"
  }'
```

Log in and save the returned JWT in the **same terminal** (requires `jq`):

```bash
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"email":"borrower.test@example.com","password":"<YOUR_PASSWORD>"}' \
  | jq -r '.token')
```

Call a protected endpoint:

```bash
curl -i http://localhost:8080/api/equipment \
  -H "Authorization: Bearer $TOKEN"
```

Create a borrowing request (replace the equipment ID and dates with valid test values):

```bash
curl -i -X POST http://localhost:8080/api/borrow-requests \
  -H 'Content-Type: application/json' \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "borrowDate": "2026-11-02T09:00:00",
    "expectedReturnDate": "2026-11-05T17:00:00",
    "equipmentIds": [3]
  }'
```

The borrower is derived from the authenticated user, not from a `borrowerId` supplied in the request body.

## Swagger / OpenAPI

With the application running:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

To test protected endpoints in Swagger UI:

1. Call `POST /api/auth/login` with valid credentials.
2. Copy the returned `token` value.
3. Click **Authorize**, paste the JWT **without** `Bearer `, and confirm.
4. Use **Try it out** on a permitted endpoint.

The Swagger UI and OpenAPI JSON routes should be accessible without a JWT, while protected API operations require one.

## Error handling

| HTTP status | Meaning | Example |
| --- | --- | --- |
| `200 OK` | Successful retrieval or update | Retrieve a request |
| `201 Created` | Resource created | Create a borrowing request |
| `204 No Content` | Successful deletion | Delete a record |
| `400 Bad Request` | Invalid request data | Missing required borrow date |
| `401 Unauthorized` | Missing/invalid authentication (target behavior) | Expired JWT |
| `403 Forbidden` | Insufficient permissions | Borrower accesses another borrower's request |
| `404 Not Found` | Resource missing | Unknown request ID |
| `409 Conflict` | Business rule violation | Cancel a completed request |

Custom exceptions include `ResourceNotFoundException` and `BusinessRuleException`. Spring Security's `AccessDeniedException` handles ownership violations. Error bodies are currently simple and need not share a standardized JSON envelope.

## Testing

**Manually exercised:** authentication with Bearer JWTs, ownership and role checks, validation/error responses (`400`, `403`, `404`, `409`), maintenance condition transitions, and a simulated transaction rollback.

**Automated testing:** JUnit 5 and Mockito work has begun, but the full service/controller/integration suite remains planned.

```bash
./mvnw test
```

This command runs the tests currently present in the repository; it does not imply all planned test cases are implemented or passing.

## Roadmap

- [x] REST API, entities, DTOs, repositories, and CRUD workflows
- [x] JWT login and role-based security
- [x] Borrower ownership checks
- [x] Exception handling for validation, missing resources, conflicts, and access denial
- [x] Transaction annotations and manual rollback verification
- [x] Swagger UI with JWT Bearer support
- [ ] Audit and tighten authorization for user-management and maintenance CRUD endpoints
- [ ] Expand JUnit, Mockito, MockMvc, and integration tests
- [ ] Add database migrations with Flyway
- [ ] Improve JWT failure responses and configuration/secrets management
- [ ] Add pagination, filtering, and sorting where appropriate
- [ ] Dockerize API and PostgreSQL
- [ ] Set up CI with GitHub Actions
- [ ] Deploy with HTTPS, logging, and health monitoring

## Security notes

- Never commit JWT signing secrets, production database credentials, or live tokens.
- Public signup should not allow callers to assign themselves elevated roles.
- Enforce uniqueness of user email addresses at the database level; duplicate test accounts were observed during development.
- Review authorization for all user-management and maintenance endpoints before public deployment.
- `@Transactional` helps with atomic updates but does not by itself prevent concurrent reservation races; locking/versioning may be needed.
- The project is intended as a learning/portfolio backend and has not been presented as production hardened.
