# EventDrivenOMS

An event-driven Order Management System (OMS) built with Spring Boot microservices, Apache Kafka, and Docker.

## Architecture

The system is composed of the following microservices, communicating asynchronously through Kafka topics:

```
Client
  |
  v
API Gateway (port 8081)
  |
  +-- Order Service (port 8082)        --> Publishes: order-events
  |
  +-- Inventory Service (port 8083)    --> Consumes: order-events
  |                                    --> Publishes: inventory-events
  |
  +-- Payment Service (port 8086)      --> Consumes: inventory-events
  |                                    --> Publishes: payment-completed
  |
  +-- Notification Service (port 8084) --> Consumes: order-events, inventory-events
```

## Services

| Service              | Port  | Description                                    |
|----------------------|-------|------------------------------------------------|
| api-gateway          | 8081  | Spring Cloud Gateway - single entry point      |
| service-discovery    | 8761  | Eureka Server - service registry               |
| order-service        | 8082  | Accepts orders, persists to PostgreSQL         |
| inventory-service    | 8083  | Reserves stock on order creation               |
| payment-service      | 8086  | Processes payment after stock reservation      |
| notification-service | 8084  | Logs in-app notifications on order events      |
| common-lib           | -     | Shared DTOs, events, and enums (Maven library) |

## Infrastructure

| Component  | Image                           | Port |
|------------|---------------------------------|------|
| PostgreSQL  | postgres:16-alpine              | 5432 |
| Kafka       | confluentinc/cp-kafka:7.7.0     | 9092 |
| Zookeeper   | confluentinc/cp-zookeeper:7.7.0 | 2181 |
| Kafka UI    | provectuslabs/kafka-ui:latest   | 8085 |
| Redis       | redis:7-alpine                  | 6379 |

## Tech Stack

- **Java 17**
- **Spring Boot 3.x**
- **Spring Cloud (Eureka, Gateway)**
- **Apache Kafka**
- **PostgreSQL**
- **Redis**
- **Lombok**
- **Docker / Docker Compose**

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- Docker Desktop

### Run Infrastructure

Start all infrastructure services (Kafka, PostgreSQL, Redis, Eureka, etc.):

```bash
docker-compose up -d
```

### Build common-lib first

```bash
cd common-lib
mvn clean install
```

### Run Services

Each service can be started individually from its directory:

```bash
cd order-service
mvn spring-boot:run
```

Repeat for `inventory-service`, `payment-service`, `notification-service`, `api-gateway`.

## Kafka Topics

| Topic             | Producer          | Consumer(s)                             |
|-------------------|-------------------|-----------------------------------------|
| order-events      | order-service     | inventory-service, notification-service |
| inventory-events  | inventory-service | payment-service, notification-service   |
| payment-completed | payment-service   | notification-service                    |

## API Endpoints

### Order Service (via API Gateway at `localhost:8081`)

| Method | Endpoint                           | Description       |
|--------|------------------------------------|-------------------|
| POST   | /orderService/createOrder          | Place a new order |
| GET    | /orderService/getOrder?orderId=... | Retrieve an order |

### Inventory Service (via API Gateway at `localhost:8081`)

| Method | Endpoint         | Description                |
|--------|------------------|----------------------------|
| POST   | /inventory/add   | Add stock for a product    |
| GET    | /inventory/check | Check stock by productId   |
| GET    | /inventory/all   | List all inventory records |

## Environment Variables

The following values are configured in each service's `application.properties` for local development.
**Do not use these credentials in production.**

| Variable                       | Default Value  | Notes                    |
|--------------------------------|----------------|--------------------------|
| spring.datasource.username     | postgres       | Local dev only           |
| spring.datasource.password     | postgres       | Local dev only           |
| spring.kafka.bootstrap-servers | localhost:9092 | Overridden in Docker env |

## Project Structure

```
EventDrivenOMS1/
├── api-gateway/
├── common-lib/
├── inventory-service/
├── notification-service/
├── order-service/
├── payment-service/
├── ServiceDiscovery/
├── docker-compose.yml
└── README.md
```
