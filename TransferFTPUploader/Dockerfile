FROM openjdk:12-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=build/libs/TransferFTPUploader.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT exec java ${JAVA_OPTS} -jar /app.jar
