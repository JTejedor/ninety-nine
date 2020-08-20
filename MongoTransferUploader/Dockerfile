FROM openjdk:12-jdk-alpine
VOLUME /tmp
COPY MongoTransferUploader.jar MongoTransferUploader.jar
ENTRYPOINT exec java ${JAVA_OPTS} -jar MongoTransferUploader.jar
