FROM eclipse-temurin:25-jdk AS build

WORKDIR /home/gradle/account

COPY gradle /home/gradle/account/gradle
COPY gradlew /home/gradle/account/gradlew
COPY gradle/wrapper/gradle-wrapper.properties /home/gradle/account/gradle/wrapper/gradle-wrapper.properties

RUN chmod +x /home/gradle/account/gradlew

COPY build.gradle /home/gradle/account/build.gradle
COPY src /home/gradle/account/src

RUN ./gradlew clean bootJar --no-daemon

FROM eclipse-temurin:25-jre AS runtime
WORKDIR /app

COPY --from=build /home/gradle/account/build/libs/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/app.jar"]