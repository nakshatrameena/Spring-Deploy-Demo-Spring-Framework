# Build stage
FROM eclipse-temurin:26-jdk-jammy AS builder
WORKDIR /build
COPY . .
RUN ./mvnw clean package -DskipTests --no-transfer-progress

# Runtime stage (slim, secure, fast startup)
FROM eclipse-temurin:26-jre-jammy
WORKDIR /app

# Create non-root user (security)
RUN groupadd -r appuser && useradd -r -g appuser appuser

COPY --from=builder /build/target/*.jar app.jar

# Set ownership and permissions
RUN chown -R appuser:appuser /app
USER appuser

EXPOSE 8080
EXPOSE 8081

# JVM flags for containers (memory awareness + virtual threads friendly)
ENTRYPOINT ["java", \
    "-XX:MaxRAMPercentage=75.0", \
    "-XX:+UseG1GC", \
    "-XX:+UseStringDeduplication", \
    "-jar", \
    "app.jar"]

    # ... after COPY . .
WORKDIR /build
COPY . .

# ADD THIS LINE:
RUN chmod +x mvnw

# Then the next line will work
RUN ./mvnw clean package -DskipTests --no-transfer-progress