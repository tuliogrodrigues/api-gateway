# API Gateway

A reactive API Gateway implementation demonstrating Spring Cloud Gateway with Spring WebFlux microservices.

## Architecture

This project showcases a microservices architecture built entirely on reactive Spring technologies:

```
                    ┌─────────────────┐
                    │  API Gateway    │
                    │ (Port 8080)     │
                    └────────┬────────┘
                             │
              ┌──────────────┴──────────────┐
              │                             │
              ▼                             ▼
     ┌────────────────┐         ┌────────────────┐
     │  User Service  │         │ Account Service│
     │  (Port 8082)   │         │  (Port 8081)   │
     └────────────────┘         └────────────────┘
```

## Components

### API Gateway
- **Spring Cloud Gateway** - Reactive API Gateway (successor to Netflix Zuul)
- **Rector** - Reactive HTTP client for service communication
- **Resilience4j** - Circuit breaker pattern for fault tolerance
- **SpringDoc OpenAPI** - OpenAPI/Swagger documentation

### Microservices
- **User Service** - Spring WebFlux reactive REST API
- **Account Service** - Spring WebFlux reactive REST API

## Key Technologies

| Component | Technology |
|----------|------------|
| Gateway | Spring Cloud Gateway (WebFlux) |
| Circuit Breaker | Resilience4j |
| Service Communication | Reactor (WebClient) |
| Documentation | OpenAPI 3.0 |

## Routing

The gateway routes incoming requests to backend services:

| Path | Backend Service |
|------|----------------|
| `/users/**` | user-service:8082 |
| `/accounts/**` | account-service:8081 |

## Building

```bash
./gradlew build
```

## Running

Start each service:

```bash
# API Gateway
java -jar api-gateway/build/libs/api-gateway-0.0.1-SNAPSHOT.jar

# User Service  
java -jar user-service/build/libs/user-service-0.0.1-SNAPSHOT.jar --server.port=8082

# Account Service
java -jar account-service/build/libs/account-service-0.0.1-SNAPSHOT.jar --server.port=8081
```

Or use Docker Compose for orchestration.

## API Documentation

Once running, access the OpenAPI documentation at:
```
http://localhost:8080/swagger-ui.html
```

## Design Patterns Demonstrated

- **API Gateway** - Central entry point for all client requests
- **Circuit Breaker** - Fault tolerance and resilience
- **Reactive Streams** - Non-blocking, event-driven architecture