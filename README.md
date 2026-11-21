# Asset Management System

A Spring Boot REST API for tracking company equipment — what's available,
what's assigned to whom, and the full assignment history.

## What it does

Employees are registered; equipment (laptops, monitors, phones) is catalogued;
equipment is assigned to employees and later returned. The system enforces the
rules: equipment can only be assigned if it's available, can't be double-assigned,
and can't be assigned if retired. Reports show equipment by status and how many
active items each employee has.

## Architecture

    HTTP client
        │
        ▼
    Controller   → receives requests, validates, returns DTOs
        │
        ▼
    Service      → business rules, @Transactional boundaries
        │
        ▼
    Repository   → Spring Data JPA
        │
        ▼
    MySQL        → hand-written schema (schema.sql)

- **DTOs** keep entities from leaking into the API contract.
- **Global exception handler** returns a consistent error shape (404 / 400 / 409).
- **Transactions** keep assignment writes atomic.

## Domain model

- `Employee` — id, name, email (unique), department
- `Equipment` — id, assetTag (unique), type, brand, purchaseDate, status (enum)
- `Assignment` — id, employee, equipment, assignedDate, returnedDate (null = active)

(`Assignment` is a separate entity rather than a flag on equipment, because it
preserves the history of who had what.)

## Endpoints

| Method | Path | Purpose |
|---|---|---|
| GET    | /api/employees | list, paginated |
| GET    | /api/employees/{id} | one employee |
| POST   | /api/employees | create |
| PUT    | /api/employees/{id} | update |
| DELETE | /api/employees/{id} | delete |
| GET    | /api/employees/{id}/assignments | active items for an employee |
| GET    | /api/equipment?status= | list, filtered + paginated |
| POST   | /api/equipment | create (starts AVAILABLE) |
| PUT    | /api/equipment/{id} | update (status not client-settable) |
| DELETE | /api/equipment/{id} | delete |
| POST   | /api/assignments | assign equipment to employee |
| PUT    | /api/assignments/{id}/return | return it |
| GET    | /api/assignments/active | current active assignments |
| GET    | /api/equipment/report/by-status | equipment count by status |
| GET    | /api/assignments/report/active-by-employee | active count per employee |

## Running it

    # 1. create schema + seed
    mysql -u root -p < schema.sql
    mysql -u root -p < seed.sql

    # 2. configure your DB password in src/main/resources/application.properties

    # 3. run
    ./mvnw spring-boot:run

See `requests.md` for ready-to-run curl examples.

## Notes / known limitations

- No authentication (see "future work" below).
- `spring.jpa.hibernate.ddl-auto=none` — the committed `schema.sql` is the source of truth.

## Future work

- Spring Security with roles (admin / operator)
- Frontend
- Docker Compose for one-command startup