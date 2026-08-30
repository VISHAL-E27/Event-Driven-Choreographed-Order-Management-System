# common-lib

Shared Maven library used by all microservices. Contains DTOs, event classes, and enums to ensure consistency across service boundaries.

## Usage

This is not a runnable service. It must be installed to the local Maven repository before building other services:

```bash
cd common-lib
mvn clean install
```

## Contents

### DTOs

| Class          | Description                                  |
|----------------|----------------------------------------------|
| `ApiResponse`  | Standard wrapper for all REST responses      |
| `OrderItemDto` | Represents a single item in an order         |

### Events (Kafka Payloads)

| Class                   | Published By      | Consumed By                              |
|-------------------------|-------------------|------------------------------------------|
| `OrderCreatedEvent`     | order-service     | inventory-service, notification-service  |
| `InventoryReservedEvent`| inventory-service | payment-service, notification-service    |
| `PaymentCompletedEvent` | payment-service   | notification-service                     |

### Enums

| Enum            | Values                                             |
|-----------------|----------------------------------------------------|
| `OrderStatus`   | PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, FAILED, CANCELLED |
| `PaymentStatus` | PENDING, SUCCESS, FAILED, REFUNDED                 |
