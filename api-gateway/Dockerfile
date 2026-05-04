FROM gradle:jdk25-ubi10 AS build

WORKDIR /app

COPY build.gradle build.gradle
COPY settings.gradle settings.gradle
RUN gradle dependencies --no-daemon

COPY src src
RUN gradle clean bootJar --no-daemon

FROM eclipse-temurin:25-jre AS runtime
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]