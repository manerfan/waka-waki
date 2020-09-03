FROM openjdk:11.0
MAINTAINER manerfan
ARG SERVICE_NAME
ENV JVM_XMS=1g JVM_XMX=1g JVM_XMN=512m
COPY waka-waki-services/$SERVICE_NAME.jar /opt/application.jar
COPY waka-waki-services/docker-startup.sh /docker-startup.sh
ENTRYPOINT ["/docker-startup.sh"]
