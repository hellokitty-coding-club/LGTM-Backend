FROM eclipse-temurin:17

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

CMD ["java", "-Dspring.profiles.active=dev", "-jar", "app.jar"]