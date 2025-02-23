FROM openjdk:17
ARG JAR_FILE=module-api/build/libs/*.jar
COPY ${JAR_FILE} app-api-dev.jar
cp /home/ubuntu/config/tripot-534cb-firebase-adminsdk-au4ch-b8effba143.json module-api/build/libs/
COPY tripot-534cb-firebase-adminsdk-au4ch-b8effba143.json /tripot-534cb-firebase-adminsdk-au4ch-b8effba143.json
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=dev","app-api-dev.jar"]