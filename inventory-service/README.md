# inventory-service

Manages product stock. Listens for `OrderCreatedEvent` from Kafka, reserves stock, and publishes `InventoryReservedEvent`.

## Port

`8083`

## Responsibilities

- Add and track product stock via REST
- Consume `OrderCreatedEvent` and attempt to reserve stock
- Publish `InventoryReservedEvent` (with `stockAvailable` true/false) to `inventory-events`

## Kafka

| Role     | Topic             | Event                 |
|----------|-------------------|-----------------------|
| Consumer | order-events      | OrderCreatedEvent     |
| Producer | inventory-events  | InventoryReservedEvent|

## REST API

| Method | Endpoint         | Description                |
|--------|------------------|----------------------------|
| POST   | /inventory/add   | Add stock for a product    |
| GET    | /inventory/check | Check stock by productId   |
| GET    | /inventory/all   | List all inventory records |

## Dependencies

- `common-lib` (shared DTOs and events)
- PostgreSQL
- Kafka
- Eureka (service discovery)

## Configuration

See `src/main/resources/application.properties`.
Key properties:

```properties
server.port=8083
spring.datasource.url=jdbc:postgresql://localhost:5432/order_db
spring.kafka.bootstrap-servers=localhost:9092
```
