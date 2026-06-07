# ADR 001: Microservices Architecture

## Status
Accepted

## Context
Cortex needs to demonstrate production-grade distributed systems patterns. A monolith would be simpler but wouldn't showcase event-driven communication, service isolation, or API Gateway patterns.

## Decision
We will use a multi-module Maven monorepo with 5 Spring Boot services:

- **Gateway** — API Gateway (Spring Cloud Gateway)
- **User Service** — Authentication and user management
- **Catalog Service** — Product catalog CRUD
- **Order Service** — Order processing and event publishing
- **Notification Service** — Event-driven notifications

## Consequences
- Each service can be developed, tested, and scaled independently
- The monorepo keeps cross-service contracts visible and co-versioned
- Additional overhead from running multiple JVMs during development
- Services communicate through events, enabling eventual consistency
