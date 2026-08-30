# ServiceDiscovery

Eureka Server for service registration and discovery across all microservices.

## Port

`8761`

## Responsibilities

- Act as the Eureka service registry
- All microservices register themselves here on startup
- API Gateway queries Eureka to resolve service URLs dynamically

## Dashboard

Once running, the Eureka dashboard is accessible at:

```
http://localhost:8761
```

## Configuration

See `src/main/resources/application.properties`.
Key properties:

```properties
server.port=8761
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
```
