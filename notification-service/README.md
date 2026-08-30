# notification-service

Listens to Kafka events and logs notifications for order lifecycle updates. Acts as an in-app notification layer.

## Port

`8084`

## Responsibilities

- Consume `OrderCreatedEvent` from `order-events` topic and log order received notification
- Consume `InventoryReservedEvent` from `inventory-events` topic and log order confirmed or cancelled notification

## Kafka

| Role     | Topic            | Event                  |
|----------|------------------|------------------------|
| Consumer | order-events     | OrderCreatedEvent      |
| Consumer | inventory-events | InventoryReservedEvent |

## Notes

- This service has no REST endpoints — it is purely event-driven
- Currently uses `log.info` / `log.warn` as the notification mechanism
- Can be extended to send emails (e.g., via Spring Mail or SendGrid) by updating `NotificationService`

## Dependencies

- `common-lib` (shared DTOs and events)
- Kafka
- Eureka (service discovery)

## Configuration

See `src/main/resources/application.properties`.
Key properties:

```properties
server.port=8084
spring.kafka.bootstrap-servers=localhost:9092
```
