FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY build.gradle settings.gradle ./

COPY src ./src

RUN ./gradlew bootJar --no-daemon

EXPOSE 9000

ENTRYPOINT ["java","-jar","build/libs/goodfood-auth-server-0.0.1-SNAPSHOT.jar"]