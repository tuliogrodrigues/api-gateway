FROM eclipse-temurin:25-jdk AS build

WORKDIR /home/gradle/gateway

COPY gradle /home/gradle/gateway/gradle
COPY gradlew /home/gradle/gateway/gradlew
COPY gradle/wrapper/gradle-wrapper.properties /home/gradle/gateway/gradle/wrapper/gradle-wrapper.properties

RUN chmod +x /home/gradle/gateway/gradlew

COPY build.gradle /home/gradle/gateway/build.gradle
COPY src /home/gradle/gateway/src

RUN ./gradlew clean bootJar --no-daemon

FROM eclipse-temurin:25-jre AS runtime
WORKDIR /app

COPY --from=build /home/gradle/gateway/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]