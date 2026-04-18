FROM gradle:7.6-jdk17 AS builder
WORKDIR /build
COPY build.gradle settings.gradle ./
COPY src src
RUN gradle bootJar --no-daemon

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /build/build/libs/user-service.jar /app/app.jar
EXPOSE 8089
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
