# Stage 1: Build Stage (Oracle provides the fastest updates for new JDKs)
FROM container-registry.oracle.com/java/openjdk:26 AS builder
WORKDIR /build
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Stage 2: Runtime Stage
FROM container-registry.oracle.com/java/openjdk:26
WORKDIR /app
COPY --from=builder /build/target/*.jar app.jar

# Railway uses a dynamic PORT variable
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=${PORT:8080}"]