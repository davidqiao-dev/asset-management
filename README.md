# Asset Management API

Spring Boot backend for tracking company equipment and employee assignments.

## What it does

Employees are registered; equipment (laptops, monitors, phones) is catalogued;
equipment is assigned to employees and later returned. The system enforces the
rules: equipment can only be assigned if it's available, can't be double-assigned,
and can't be assigned if retired. Reports show equipment by status and how many
active items each employee has.

## Stack

Java · Spring Boot · Spring Security (JWT) · Spring Data JPA · MySQL · springdoc OpenAPI

**Why JWT:** stateless auth — no server-side session store, so the API scales
horizontally; the token carries identity and expiry, verified per request.

## Authorization model

Any authenticated user can read, create, update, assign, and return.
Only ADMIN can delete. Deletes of referenced records return 409.

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

| Method         | Path                                       | Purpose                        |
|----------------|--------------------------------------------|--------------------------------|
| POST           | /api/auth/login                            | obtain JWT                     |
| GET/POST       | /api/equipment                             | list / create                  |
| GET/PUT/DELETE | /api/equipment/{id}                        | read / update / delete (ADMIN) |
| GET            | /api/equipment/report/by-status            | counts                         |
| GET/POST       | /api/employees                             | list / create                  |
| GET/PUT/DELETE | /api/employees/{id}                        | read / update / delete (ADMIN) |
| GET            | /api/employees/{id}/assignments            | per-employee history           |
| POST           | /api/assignments                           | assign equipment               |
| PUT            | /api/assignments/{id}/return               | mark returned                  |
| GET            | /api/assignments/active                    | paged active                   |
| GET            | /api/assignments/report/active-by-employee | counts                         |

## Engineering notes

- Secrets are injected via environment variables (`DB_PASSWORD`, `JWT_SECRET`) —
  no secret material is in the repo or in a committed profile file. The app fails
  fast if `JWT_SECRET` is absent rather than falling back to a weak default.
- Delete of a referenced equipment originally returned an *employee* error message —
  corrected to an entity-neutral conflict message.
- `/api/assignments/active` took a raw `Pageable` while every other list endpoint
  used explicit `page`/`size`/`sort` params — unified.
- `Assignment` is a separate entity (not a flag on equipment) so assignment history
  is preserved, and a DB foreign key blocks deleting equipment that is in use.

## Running it

    # 1. create schema + seed
    mysql -u root -p < schema.sql
    mysql -u root -p < seed.sql

    # 2. provide secrets and DB config via environment variables

    export DB_PASSWORD=your-db-password
    export JWT_SECRET=your-secret-at-least-32-bytes-long
    # (Windows cmd:  set DB_PASSWORD=...  /  set JWT_SECRET=...)

    # 3. run
    ./mvnw spring-boot:run

See `requests.md` for ready-to-run curl examples.

## Tests

`./mvnw test` — 8 integration tests covering authN (401), authZ (ADMIN-only delete → 403),
FK constraint (409), and duplicate-assignment (409).

## Notes / known limitations

- `spring.jpa.hibernate.ddl-auto=none` — the committed `schema.sql` is the source of truth.


## Future work

- Frontend
- Docker Compose for one-command startup