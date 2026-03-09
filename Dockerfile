# Build stage
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /build

# Copy pom.xml và source code
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Install curl for health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Copy JAR từ build stage
COPY --from=builder /build/target/*.jar app.jar

# Expose port
EXPOSE 8000

# Health check - không cần (Spring Boot sẽ handle)
# HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
#     CMD curl -f http://localhost:8000/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

