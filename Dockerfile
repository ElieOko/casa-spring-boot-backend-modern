FROM gradle:9.1.0-jdk25-corretto-al2023 AS builder

WORKDIR /builder
ADD . /builder
FROM openjdk:26-slim-bookworm
# Répertoire de travail
WORKDIR /app
COPY . .
RUN chmod +x gradlew
#RUN ./gradlew clean build -x test

EXPOSE 8083
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} /app/casa.jar
ENTRYPOINT ["java", "-jar", "/app/casa.jar"]
# docker run --publish 8083:8085 elieoko/casa-spring-app