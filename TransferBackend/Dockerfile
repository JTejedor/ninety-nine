FROM openjdk:12-jdk-alpine
VOLUME /tmp
COPY TransferBackend.jar TransferBackend.jar
ENTRYPOINT exec java ${JAVA_OPTS} -jar TransferBackend.jar
