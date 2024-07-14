# Use OpenJDK 17 as base image
FROM azul/zulu-openjdk-alpine:17

# Set working directory inside the container
WORKDIR /app

# Copy the JAR file into the container at /app
COPY ./target/voting-app-v1-0.0.1-SNAPSHOT.jar /app/application.jar

# Command to run the JAR file
CMD ["java", "-jar", "application.jar"]