# ADR 004: Database per Service

## Status
Accepted

## Context
Each microservice should own its data to avoid tight coupling between services.

## Decision
Each service has its own H2 in-memory database (in-memory for development, PostgreSQL for production). The `common` module contains shared DTOs and events but no shared persistence.

- **User Service** → `userdb`
- **Catalog Service** → `catalogdb`
- **Order Service** → `orderdb`

No service accesses another service's database directly. Cross-service data is shared through:
- API calls (synchronous)
- Events (asynchronous)

## Consequences
- Services are decoupled at the database level
- Schema changes in one service don't affect others
- Requires distributed transaction patterns (sagas) for multi-service operations
- Development is simpler with per-service H2 databases
