# Use Java 17 base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy JAR file from target folder
COPY target/*.jar app.jar

# Expose port (Spring Boot default)
EXPOSE 8081

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]