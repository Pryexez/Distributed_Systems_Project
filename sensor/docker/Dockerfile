
##
## Build stage
##

FROM maven:3.8.1-openjdk-11-slim AS build
LABEL stage build

RUN mkdir -p /app
COPY sensor/sources/ /app/
WORKDIR /app
RUN mvn package


##
## Run stage
##
FROM openjdk:11.0-jre-slim
EXPOSE 1237/udp

RUN mkdir -p /app
COPY --from=build /app/target/udp_sensor-1.0-SNAPSHOT.jar /app
CMD [ "java", "-jar", "/app/udp_sensor-1.0-SNAPSHOT.jar" ]
