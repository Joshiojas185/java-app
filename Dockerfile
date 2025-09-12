# Start with a base image that includes Java 17
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project files to the container
COPY pom.xml .

# Copy the source code to the container
COPY src ./src

# Use the Maven wrapper to clean and package the application into a JAR file
# -DskipTests flag skips tests to speed up the build process for deployment
RUN mvn clean package -DskipTests

# Define the command to run the application when the container starts
ENTRYPOINT ["java", "-jar", "/app/target/javabackend-0.0.1-SNAPSHOT.jar"]