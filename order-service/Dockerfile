# ─── Stage 1: Build the Application ───
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app

# Copy parent POM & modules for dependency resolution
COPY EventDrivenOMS/pom.xml ./EventDrivenOMS/pom.xml
COPY common-lib ./common-lib
COPY order-service ./order-service

# Build common-lib and order-service
RUN mvn clean package -DskipTests -f EventDrivenOMS/pom.xml -pl ../order-service -am

# ─── Stage 2: Runtime Container ───
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=builder /app/order-service/target/*.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]
