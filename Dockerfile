# Stage 1: Build Stage
FROM openjdk:26-slim AS builder
WORKDIR /build

# Copy all files
COPY . .

# Fix permissions BEFORE running maven
RUN chmod +x mvnw

# Build the application
RUN ./mvnw clean package -DskipTests --no-transfer-progress

# Stage 2: Runtime stage
FROM openjdk:26-slim
WORKDIR /app

# Create non-root user for security
RUN groupadd -r appuser && useradd -r -g appuser appuser

# Copy the built jar from the builder stage
COPY --from=builder /build/target/*.jar app.jar

# Set ownership
RUN chown -R appuser:appuser /app
USER appuser

# Render uses dynamic ports; Spring Boot needs to know this
EXPOSE 8080

# Optimized JVM flags for Render's Free Tier (512MB)
ENTRYPOINT ["java", \
    "-XX:MaxRAMPercentage=75.0", \
    "-jar", \
    "app.jar", \
    "--server.port=${PORT:8080}"]