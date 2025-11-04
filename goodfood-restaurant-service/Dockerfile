FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY build/libs/goodfood-restaurant-service-1.0.0.jar app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]