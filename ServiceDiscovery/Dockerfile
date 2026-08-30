# ─── Stage 1: Build the Application ───
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app

# Copy parent POM & modules for dependency resolution
COPY EventDrivenOMS/pom.xml ./EventDrivenOMS/pom.xml
COPY common-lib ./common-lib
COPY ServiceDiscovery ./ServiceDiscovery

# Build common-lib and ServiceDiscovery
RUN mvn clean package -DskipTests -f EventDrivenOMS/pom.xml -pl ../ServiceDiscovery -am

# ─── Stage 2: Runtime Container ───
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=builder /app/ServiceDiscovery/target/*.jar app.jar

EXPOSE 8761

ENTRYPOINT ["java", "-jar", "app.jar"]
