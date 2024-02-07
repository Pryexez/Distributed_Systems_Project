
##
## Build stage
##

FROM maven:3.8.1-openjdk-11-slim AS build
LABEL stage build


RUN mkdir -p /app
COPY thrift_database2/sources/ /app/
WORKDIR /app
RUN mvn package


##
## Run stage
##
FROM openjdk:11.0-jre-slim
EXPOSE 9999


RUN mkdir -p /app
COPY --from=build /app/target/thrift_db2-1.0-SNAPSHOT.jar /app
CMD [ "java", "-jar", "/app/thrift_db2-1.0-SNAPSHOT.jar" ]
