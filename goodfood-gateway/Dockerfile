FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY build/libs/goodfood-gateway-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8090

ENTRYPOINT ["java","-jar","/app/app.jar"]