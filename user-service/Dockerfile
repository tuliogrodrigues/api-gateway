FROM eclipse-temurin:25-jdk AS build

WORKDIR /home/gradle/user

# Copy gradle wrapper
COPY gradle /home/gradle/user/gradle
COPY gradlew /home/gradle/user/gradlew
COPY gradle/wrapper/gradle-wrapper.properties /home/gradle/user/gradle/wrapper/gradle-wrapper.properties

RUN chmod +x /home/gradle/user/gradlew

COPY build.gradle /home/gradle/user/build.gradle
COPY src /home/gradle/user/src

RUN ./gradlew clean bootJar --no-daemon

FROM eclipse-temurin:25-jre AS runtime
WORKDIR /app

COPY --from=build /home/gradle/user/build/libs/*.jar app.jar

EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app/app.jar"]