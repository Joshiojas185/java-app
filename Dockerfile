# # Start with a base image that includes Java 17
# # FROM openjdk:17-jdk-slim
# FROM maven:3.9.6-openjdk-17-slim

# # Set the working directory inside the container
# WORKDIR /app

# # Copy the Maven project files to the container
# COPY pom.xml .

# # Copy the source code to the container
# COPY src ./src

# # Use the Maven wrapper to clean and package the application into a JAR file
# # -DskipTests flag skips tests to speed up the build process for deployment
# RUN mvn clean package -DskipTests

# # Define the command to run the application when the container starts
# ENTRYPOINT ["java", "-jar", "/app/target/javabackend-0.0.1-SNAPSHOT.jar"]






# Use a base image that includes both Maven and OpenJDK 17.
# This solves the "mvn: not found" error you were seeing.
FROM maven:3.9.6-openjdk-17-slim

# Set the working directory inside the container.
WORKDIR /app

# Copy the Maven project files (pom.xml) into the container.
# This is a best practice to take advantage of Docker's layer caching.
COPY pom.xml .

# Copy the source code into the container.
COPY src ./src

# Use Maven to clean and package the application into a JAR file.
# The -DskipTests flag is used to speed up the build process.
RUN mvn clean package -DskipTests

# Define the command to run the application when the container starts.
# Update the JAR name to match your project's artifactId and version.
ENTRYPOINT ["java", "-jar", "/app/target/javabackend-0.0.1-SNAPSHOT.jar"]