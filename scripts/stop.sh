#!/bin/bash

ROOT_PATH="/home/ubuntu/tripot_cicd"
API_JAR="$ROOT_PATH/api_application.jar"
STOP_LOG="$ROOT_PATH/api_stop.log"
SERVICE_PID=$(pgrep -f $API_JAR) # 실행중인 Spring 서버의 PID

NOW=$(date +%c)

if [ -z "$SERVICE_PID" ]; then
  echo "[$NOW] > 서비스 NotFound" >> $STOP_LOG
else
  echo "[$NOW] > 서비스 종료 " >> $STOP_LOG
  kill "$SERVICE_PID"
  # kill -9 $SERVICE_PID # 강제 종료를 하고 싶다면 이 명령어 사용
fi