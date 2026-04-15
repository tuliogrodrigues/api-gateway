FROM gradle:9.4.1-jdk25 AS build

WORKDIR /home/gradle/account

COPY build.gradle /home/gradle/account/build.gradle
COPY src /home/gradle/account/src

RUN gradle clean bootJar --no-daemon

FROM eclipse-temurin:25-jre AS runtime
WORKDIR /app

COPY --from=build /home/gradle/account/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
