#!/bin/bash

set -x

JAVA_OPT="-server -Xms${JVM_XMS} -Xmx${JVM_XMX} -Xmn${JVM_XMN}"
JAVA_OPT="${JAVA_OPT} -jar -Dspring.profiles.active=prod /opt/application.jar"
JAVA_OPT="${JAVA_OPT} ${JAVA_OPT_EXT}"

mkdir -p /var/logs/${SERVICE_NAME}

echo "${SERVICE_NAME} is starting, you can check the /var/logs/${SERVICE_NAME}/start.out"
echo "java ${JAVA_OPT}" > /var/logs/${SERVICE_NAME}/start.out 2>&1 &
nohup java ${JAVA_OPT} > /var/logs/${SERVICE_NAME}/start.out 2>&1 < /dev/null
