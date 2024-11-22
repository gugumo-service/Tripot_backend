#!/bin/bash

ROOT_PATH="/home/ubuntu/tripot_cicd"
API_JAR="$ROOT_PATH/api_application.jar"

API_APP_LOG="$ROOT_PATH/api_application.log"
API_ERR_LOG="$ROOT_PATH/api_error.log"
API_START_LOG="$ROOT_PATH/api_start.log"

NOW=$(date +%c)

echo "[$NOW] $API_JAR 복사" >> $API_START_LOG
cp $ROOT_PATH/module-api/build/libs/*.jar $API_JAR

echo "[$NOW] > $API_JAR 실행" >> $API_START_LOG
nohup java -jar -Dspring.profiles.active=prod $API_JAR > $API_APP_LOG 2> $API_ERR_LOG &

SERVICE_PID=$(pgrep -f $API_JAR)

echo "[$NOW] > 서비스 PID: $SERVICE_PID" >> $API_START_LOG