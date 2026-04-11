FROM gradle:9.4.1-jdk25 AS build

WORKDIR /home/gradle/project

COPY build.gradle /home/gradle/project
COPY src /home/gradle/project/src

RUN gradle clean bootJar --no-daemon

FROM eclipse-temurin:25-jre AS runtime
WORKDIR /app

COPY --from=build /home/gradle/project/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
