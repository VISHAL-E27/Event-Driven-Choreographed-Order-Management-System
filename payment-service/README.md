# payment-service

Processes payments after inventory reservation. Consumes `InventoryReservedEvent` and publishes `PaymentCompletedEvent`.

## Port

`8086`

## Responsibilities

- Consume `InventoryReservedEvent` from Kafka
- If stock is available, create a payment record in PostgreSQL and mark payment as successful
- If stock is unavailable, publish a failed `PaymentCompletedEvent` immediately
- Publish `PaymentCompletedEvent` to the `payment-completed` Kafka topic

## Kafka

| Role     | Topic             | Event                  |
|----------|-------------------|------------------------|
| Consumer | inventory-events  | InventoryReservedEvent |
| Producer | payment-completed | PaymentCompletedEvent  |

## Dependencies

- `common-lib` (shared DTOs and events)
- PostgreSQL
- Kafka
- Eureka (service discovery)

## Configuration

See `src/main/resources/application.properties`.
Key properties:

```properties
server.port=8086
spring.datasource.url=jdbc:postgresql://localhost:5432/order_db
spring.kafka.bootstrap-servers=localhost:9092
```
