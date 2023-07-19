FROM openjdk:18-jdk-alpine

# Install Maven
RUN apk update && apk add maven

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml file to the working directory
COPY pom.xml .

# Resolve project dependencies (cache the dependencies layer)
RUN mvn dependency:go-offline -B

# Copy the application code to the container
COPY src ./src

# Build the application
RUN mvn package

# Set the entry point command for the container
ENTRYPOINT ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]
