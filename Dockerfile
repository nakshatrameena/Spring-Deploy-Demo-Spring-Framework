# Build stage
FROM eclipse-temurin:21-jdk-jammy AS builder
WORKDIR /build
COPY . .
RUN ./mvnw clean package -DskipTests --no-transfer-progress

# Runtime stage (slim, secure, fast startup)
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Create non-root user (security)
RUN groupadd -r appuser && useradd -r -g appuser appuser

COPY --from=builder /build/target/*.jar app.jar

# Set ownership and permissions
RUN chown -R appuser:appuser /app
USER appuser

EXPOSE 8080

# JVM flags for containers (memory awareness + virtual threads friendly)
ENTRYPOINT ["java", \
    "-XX:MaxRAMPercentage=75.0", \
    "-XX:+UseG1GC", \
    "-XX:+UseStringDeduplication", \
    "-jar", \
    "app.jar", "--server.port=${PORT:8080}"]

    FROM sapmachine:26-jdk-ubuntu-noble AS builder

    RUN chmod +x mvnw && ./mvnw clean package -DskipTests

    FROM sapmachine:26-jre-ubuntu-noble

    FROM container-registry.oracle.com/java/openjdk:26 AS builder

    RUN ./mvnw clean package -DskipTests

    FROM container-registry.oracle.com/java/openjdk:26