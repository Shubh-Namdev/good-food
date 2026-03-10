# Use a lightweight JDK 21 image
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY build/libs/goodfood-auth-server-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
