# Use the official Kotlin image
FROM openjdk:21-jdk-slim as build

# Set the working directory
WORKDIR /server

# Copy the build.gradle.kts and settings.gradle.kts
COPY build.gradle.kts settings.gradle.kts ./

# Copy the source code
COPY src ./src

# Build the application
RUN ./gradlew build -x test

# Second stage to create the final image
FROM openjdk:21-jre-slim

# Set the working directory
WORKDIR /server

# Copy the built JAR file from the previous stage
COPY --from=build /server/build/libs/*.jar server.jar

# Expose the port your application runs on
EXPOSE 8080

ENV DISCORD_BOT_TOKEN="YOUr TOKEN"

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]