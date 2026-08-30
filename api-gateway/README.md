# api-gateway

Spring Cloud Gateway acting as the single entry point for all external client requests. Routes traffic to downstream microservices discovered via Eureka.

## Port

`8081`

## Responsibilities

- Route incoming HTTP requests to the appropriate microservice
- Integrate with Eureka for dynamic service discovery and load balancing
- Expose actuator endpoints for health and gateway monitoring

## Configured Routes

| Route ID          | URI                    | Path Predicate     |
|-------------------|------------------------|--------------------|
| order-service     | lb://order-service     | /orderService/**   |
| inventory-service | lb://inventory-service | /inventory/**      |
| payment-service   | lb://payment-service   | /payment/**        |

## Dependencies

- Eureka (service discovery)
- Spring Cloud Gateway

## Configuration

See `src/main/resources/application.properties`.
Key properties:

```properties
server.port=8081
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
spring.cloud.gateway.discovery.locator.enabled=true
```
