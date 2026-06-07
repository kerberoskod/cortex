# ADR 003: API Gateway Pattern

## Status
Accepted

## Context
Clients need a single entry point to the system. Each service shouldn't handle authentication independently.

## Decision
Spring Cloud Gateway sits at port 8080 and:
1. Routes requests to the appropriate service based on path prefix
2. Validates JWT tokens for all routes except `/api/users/register` and `/api/users/login`
3. Forwards authenticated user info as `X-User-Id` and `X-User-Email` headers to downstream services

The `JwtAuthGatewayFilterFactory` is a custom GatewayFilter that:
- Skips authentication for excluded paths
- Extracts and validates the Bearer token
- Adds user headers to the proxied request
- Returns 401 for invalid or missing tokens

## Consequences
- Single entry point simplifies client integration
- Cross-cutting concerns (auth, logging, rate limiting) are centralized
- Downstream services trust the `X-User-Id` header (validated by the gateway)
- The gateway is a single point of failure (mitigated by horizontal scaling in production)
