# === Stage 1: Build the application ===
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app

# Copy the Maven wrapper and pom.xml to cache dependencies
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline -B

# Copy the source code and build the application file
COPY src ./src
RUN ./mvnw clean package -DskipTests

# === Stage 2: Create the slim runtime image ===
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the compiled JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
