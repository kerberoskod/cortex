# ADR 002: Event-Driven Communication

## Status
Accepted

## Context
Services need to communicate asynchronously. For example, when an order is created, the notification service should send a confirmation without blocking the order service.

## Decision
We use Spring's `ApplicationEventPublisher` with `@TransactionalEventListener` for in-process event delivery.

In production, this would be replaced with Apache Kafka or RabbitMQ. The event classes (`OrderCreatedEvent`, `UserRegisteredEvent`) are designed to be serializable and agnostic to the transport layer. Switching to a message broker requires only:
1. Adding the Kafka/RabbitMQ dependency
2. Adding `@EnableBinding` or equivalent configuration
3. Changing event listeners to message listeners

## Consequences
- In-process events are simple and require no infrastructure
- Events are lost on service restart (acceptable for development)
- Event contracts (`OrderCreatedEvent`, etc.) are versioned in the `common` module
- Migration to a message broker is isolated to the transport layer
