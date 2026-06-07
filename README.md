# Cortex — Event-Driven Microservices Platform

![Java](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4-6DB33F?logo=springboot&logoColor=white)
![Spring Cloud Gateway](https://img.shields.io/badge/Gateway-2024.0-6DB33F?logo=spring&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-green)

**Cortex** is a production-style e-commerce backend built with event-driven microservices, API Gateway, and JWT authentication. It demonstrates clean architecture, domain-driven service boundaries, and asynchronous communication patterns.

```
┌─────────────────────────────────────────────────────────────┐
│                        Client                               │
└──────────────────────────┬──────────────────────────────────┘
                           │
                           ▼
                    ┌──────────┐
                    │  Gateway  │  Spring Cloud Gateway (8080)
                    │  JWT Auth │
                    └──┬───┬───┘
                       │   │
              ┌────────┘   └────────┐
              ▼                      ▼
       ┌──────────┐          ┌──────────────┐
       │   User   │          │   Catalog    │
       │ Service  │          │   Service    │
       │  (8081)  │          │   (8082)     │
       └──────────┘          └──────────────┘
              │                      │
              │         Event        │
              │         Bus          │
              ├──────────────────────┤
              │                      │
              ▼                      ▼
       ┌──────────┐          ┌──────────────┐
       │  Order   │          │ Notification │
       │ Service  │─────────▶│   Service    │
       │  (8083)  │  Events  │   (8084)     │
       └──────────┘          └──────────────┘
```

## Architecture

| Component | Technology | Port | Responsibility |
|-----------|-----------|------|----------------|
| **Gateway** | Spring Cloud Gateway | 8080 | Routing, JWT validation, centralized auth |
| **User Service** | Spring Boot + JPA | 8081 | Registration, login, user management |
| **Catalog Service** | Spring Boot + JPA | 8082 | Product CRUD, inventory |
| **Order Service** | Spring Boot + JPA | 8083 | Order processing, event publishing |
| **Notification Service** | Spring Boot | 8084 | Event-driven email/simulation notifications |

### Communication Patterns

- **Synchronous**: REST APIs between client and services (via Gateway)
- **Asynchronous**: Spring Events (`ApplicationEventPublisher`) for cross-service communication
- **Auth**: JWT tokens validated at the Gateway, user info forwarded as `X-User-Id` headers

### Events

| Event | Publisher | Consumer |
|-------|-----------|----------|
| `UserRegisteredEvent` | User Service | Notification Service |
| `OrderCreatedEvent` | Order Service | Notification Service |

## Prerequisites

- **Java 21+** — [Download](https://adoptium.net/)
- **Maven 3.9+** — [Download](https://maven.apache.org/download.cgi)
- **Environment**: `JWT_SECRET` must be set (or a dev fallback is used with a warning)

## Quick Start

```bash
# Build all services
mvn clean package -DskipTests

# Set required env vars and start all services
export JWT_SECRET="your-strong-secret-key-here"
chmod +x run-all.sh
./run-all.sh
```

Each service starts on its own port (8080-8084). The Gateway is the single entry point.

## API Examples

```bash
# 1. Register a user
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{"email":"alice@example.com","password":"pass123","name":"Alice"}'

# 2. Login (save the token)
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{"email":"alice@example.com","password":"pass123"}'

# 3. List products (no auth needed for registration/login, but all other endpoints require it)
curl http://localhost:8080/api/products \
  -H "Authorization: Bearer <token>"

# 4. Create an order
curl -X POST http://localhost:8080/api/orders \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"items":[{"productId":1,"productName":"Wireless Headphones","quantity":1,"price":149.99}]}'
```

## Testing

```bash
# Run all tests (JWT_SECRET env var required)
export JWT_SECRET="test-secret-key-for-testing"
mvn test
```

## Architecture Decision Records

Key architectural decisions are documented in `docs/adr/`:

| ADR | Description |
|-----|-------------|
| [001](docs/adr/001-microservices-architecture.md) | Microservices architecture rationale |
| [002](docs/adr/002-event-driven-communication.md) | Event-driven communication with Spring Events |
| [003](docs/adr/003-api-gateway-pattern.md) | API Gateway with centralized JWT auth |
| [004](docs/adr/004-database-per-service.md) | Database-per-service pattern |

## Environment Variables

| Variable | Default | Service | Description |
|---|---|---|---|
| `SERVER_PORT` | `8080` | gateway | Gateway HTTP port |
| `JWT_SECRET` | *(required)* | gateway | HMAC-SHA256 key for JWT signing (no default in production) |
| `USER_SERVICE_PORT` | `8081` | user-service | HTTP port |
| `CATALOG_SERVICE_PORT` | `8082` | catalog-service | HTTP port |
| `ORDER_SERVICE_PORT` | `8083` | order-service | HTTP port |
| `NOTIFICATION_SERVICE_PORT` | `8084` | notification-service | HTTP port |

## Production Ready

- Each service uses H2 in-memory for development, easily swappable to PostgreSQL
- Docker Compose configuration included for containerized deployment
- Event bus can be migrated to Kafka/RabbitMQ by changing the transport layer
- Gateway can be scaled horizontally behind a load balancer

## License

MIT
