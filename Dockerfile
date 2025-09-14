# ---- Stage 1: Build the application ----
FROM maven:3.9.4-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy Maven descriptor first (better cache)
COPY pom.xml .
COPY src ./src

# Build the app (skip tests in CI build for faster image build)
RUN mvn clean package -DskipTests

# ---- Stage 2: Run the application ----
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Copy built jar from builder
COPY --from=builder /app/target/*.jar app.jar

# Expose Spring Boot default port
EXPOSE 8080

# Set active profile (overridden by env at runtime)
ENV SPRING_PROFILES_ACTIVE=prod

ENTRYPOINT ["java", "-jar", "app.jar"]
