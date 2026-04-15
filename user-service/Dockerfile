FROM gradle:9.4.1-jdk25 AS build

WORKDIR /home/gradle/user

COPY build.gradle /home/gradle/user/build.gradle
COPY src /home/gradle/user/src

RUN gradle clean bootJar --no-daemon

FROM eclipse-temurin:25-jre AS runtime
WORKDIR /app

COPY --from=build /home/gradle/user/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
