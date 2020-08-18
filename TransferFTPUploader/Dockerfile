FROM openjdk:12-jdk-alpine
VOLUME /tmp
COPY TransferFTPUploader.jar TransferFTPUploader.jar
ENTRYPOINT exec java ${JAVA_OPTS} -jar TransferFTPUploader.jar
