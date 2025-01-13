FROM openjdk:17
ARG JAR_FILE=module-api/build/libs/*.jar
COPY ${JAR_FILE} app-api-dev.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=dev","app-api-dev.jar"]