# API Gateway

A reactive API Gateway implementation demonstrating Spring Cloud Gateway with Spring WebFlux microservices.

## Architecture

This project showcases a microservices architecture built entirely on reactive Spring technologies:

```text
                    ┌─────────────────┐
                    │    Keycloak     │
                    │   (Port 8180)   │
                    └────────┬────────┘
                             │ (OAuth2/OIDC)
                    ┌────────▼────────┐
                    │  API Gateway    │
                    │   (Port 8080)   │
                    └────────┬────────┘
                             │
              ┌──────────────┴──────────────┐
              │                             │
              ▼                             ▼
     ┌────────────────┐         ┌────────────────┐
     │  User Service  │         │ Account Service│
     │  (Port 8082)   │         │  (Port 8081)   │
     └────────┬───────┘         └────────┬───────┘
              │                          │
              └──────────────┬───────────┘
                             ▼
                    ┌─────────────────┐
                    │   PostgreSQL    │
                    │   (Port 5432)   │
                    └─────────────────┘
```

## Components

### API Gateway
- **Spring Cloud Gateway** - Reactive API Gateway (successor to Netflix Zuul)
- **Reactor** - Reactive HTTP client for service communication
- **Resilience4j** - Circuit breaker pattern for fault tolerance
- **SpringDoc OpenAPI** - OpenAPI/Swagger documentation

### Microservices
- **User Service** - Spring WebFlux reactive REST API
- **Account Service** - Spring WebFlux reactive REST API

### Infrastructure
- **Keycloak** - Identity and Access Management (OAuth2/OIDC)
- **PostgreSQL** - Relational Database for microservices

## Key Technologies

| Component | Technology |
|----------|------------|
| Gateway | Spring Cloud Gateway (WebFlux) |
| Circuit Breaker | Resilience4j |
| Service Communication | Reactor (WebClient) |
| Documentation | OpenAPI 3.0 |
| Identity Provider | Keycloak |
| Database | PostgreSQL (R2DBC) |
| Build | Gradle multi-project |

## Routing

The gateway routes incoming requests to backend services:

| Path | Backend Service |
|------|----------------|
| `/users/**` | user-service:8082 |
| `/accounts/**` | account-service:8081 |

## Building

### Local Build

```bash
./gradlew build
```

### Docker Build

Each service can be built independently using its Dockerfile:

```bash
# Build all services
docker build -t api-gateway:latest -f api-gateway/Dockerfile .
docker build -t user-service:latest -f user-service/Dockerfile .
docker build -t account-service:latest -f account-service/Dockerfile .
```

## Running

### Local Development

Start each service:

```bash
# API Gateway
java -jar api-gateway/build/libs/api-gateway-0.0.1-SNAPSHOT.jar

# User Service
java -jar user-service/build/libs/user-service-0.0.1-SNAPSHOT.jar --server.port=8082

# Account Service
java -jar account-service/build/libs/account-service-0.0.1-SNAPSHOT.jar --server.port=8081
```

### Docker Compose

```bash
docker compose -f infra/compose.yml up
```

This will start all the microservices along with their required infrastructure (Keycloak and PostgreSQL). Keycloak is pre-configured with a realm and client for the API Gateway.

## Docker Architecture

The Docker setup uses a multi-stage build with Gradle from the root project:

- **Build stage**: Copies Gradle wrapper from root, includes all service directories for multi-project build
- **Runtime stage**: Copies the built JAR artifact

Each service Dockerfile:
- Copies `gradle/`, `gradlew`, `settings.gradle`, and all service directories
- Runs `./gradlew :service-name:bootJar` for the specific service

This approach:
- Shares Gradle wrapper across all services (single source of truth)
- Supports independent service builds
- Includes all services for Gradle multi-project resolution

## API Documentation

Once running, access the OpenAPI documentation at:
```
http://localhost:8080/swagger-ui.html
```

## Design Patterns Demonstrated

- **API Gateway** - Central entry point for all client requests
- **Circuit Breaker** - Fault tolerance and resilience
- **Reactive Streams** - Non-blocking, event-driven architecture
