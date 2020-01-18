FROM openjdk:8-alpine

VOLUME /data
EXPOSE 80
WORKDIR /data

ENTRYPOINT ./gradlew run