
##
## Build stage
##

FROM maven:3.8.1-openjdk-11-slim AS build
LABEL stage build

RUN mkdir -p /app
COPY http_Server/sources/ /app/
WORKDIR /app
RUN mvn package


##
## Run stage
##
FROM openjdk:11.0-jre-slim
EXPOSE 2424/tcp


RUN mkdir -p /app
COPY --from=build /app/http.json /app
COPY --from=build /app/target/http_server-1.0-SNAPSHOT.jar /app
CMD [ "java", "-jar", "/app/http_server-1.0-SNAPSHOT.jar" ]
