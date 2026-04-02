# Stage 1: Build
FROM container-registry.oracle.com/java/openjdk:26 AS builder
WORKDIR /build
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Stage 2: Runtime
FROM container-registry.oracle.com/java/openjdk:26
WORKDIR /app

# Copy the JAR from the builder stage
COPY --from=builder /build/target/*.jar app.jar

# CRITICAL: This is the line that fixes the "Failed to respond" error
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=${PORT:8080}"]