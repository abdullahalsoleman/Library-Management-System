# Stage 1: Build the application
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY . /app
RUN mvn clean package -DskipTests  # Builds the application, skipping tests for faster build

# Stage 2: Run the application
FROM openjdk:17-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar library-service.jar  # Copy the built JAR file
ENTRYPOINT ["java", "-jar", "library-service.jar"]
