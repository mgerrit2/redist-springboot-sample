# use the openjdk alpine als small image
# image size 344MB
#FROM openjdk:17.0.2-slim
ARG jar-file=demoRedisLocalHost-0.0.1-SNAPSHOT.jar

# image size 124MB
FROM bellsoft/liberica-runtime-container:jdk-21-slim-musl
# image size 245MB
#FROM gcr.io/distroless/java17

#FROM debian:stretch-slim

# show the constructor
MAINTAINER Gerrits Marc

# Copy build jar to docker image
COPY build/libs/demoRedisLocalHost-0.0.1-SNAPSHOT.jar demoRedisLocalHost-0.0.1-SNAPSHOT.jar

# The entry point to start
ENTRYPOINT ["java","-jar","/demoRedisLocalHost-0.0.1-SNAPSHOT.jar"]
