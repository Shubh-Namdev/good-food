# Use a lightweight JDK 21 image
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copy Gradle wrapper and build files
COPY build.gradle settings.gradle ./
COPY src ./src

# Build the application
RUN ./gradlew bootJar --no-daemon

EXPOSE 8888

# Run the Spring Boot app
ENTRYPOINT ["java","-jar","build/libs/goodfood-config-server-0.0.1-SNAPSHOT.jar"]