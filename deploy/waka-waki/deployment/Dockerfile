FROM openjdk:11.0
MAINTAINER manerfan
ARG SERVICE_NAME
COPY waka-waki-services/$SERVICE_NAME.jar /opt/application.jar
COPY waka-waki-services/docker-startup.sh /docker-startup.sh
ENTRYPOINT ["/docker-startup.sh"]
