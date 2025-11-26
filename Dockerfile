# Use official OpenJDK 17 image
FROM eclipse-temurin:17-alpine

# Argument for jar file location set by build
ARG JAR_FILE=target/*.jar

# Copy the built jar file into the container named app.jar
COPY ${JAR_FILE} app.jar

# Run the jar file
ENTRYPOINT ["java", "-jar", "/app.jar"]
