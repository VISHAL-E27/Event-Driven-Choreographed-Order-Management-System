# order-service

Handles order placement and retrieval. Persists orders to PostgreSQL and publishes `OrderCreatedEvent` to Kafka.

## Port

`8082`

## Responsibilities

- Accept order creation requests via REST
- Persist orders and order items to PostgreSQL
- Publish `OrderCreatedEvent` to the `order-events` Kafka topic

## Kafka

| Role     | Topic        | Event             |
|----------|--------------|-------------------|
| Producer | order-events | OrderCreatedEvent |

## REST API

| Method | Endpoint                           | Description       |
|--------|------------------------------------|-------------------|
| POST   | /orderService/createOrder          | Place a new order |
| GET    | /orderService/getOrder?orderId=... | Retrieve an order |

## Dependencies

- `common-lib` (shared DTOs and events)
- PostgreSQL
- Kafka
- Eureka (service discovery)

## Configuration

See `src/main/resources/application.properties`.
Key properties:

```properties
server.port=8082
spring.datasource.url=jdbc:postgresql://localhost:5432/order_db
spring.kafka.bootstrap-servers=localhost:9092
```
