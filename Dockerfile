# Image for building the application .jar
FROM maven:3.9.4-eclipse-temurin-17 AS builder

WORKDIR /build

COPY pom.xml .
COPY src/main ./src/main

RUN mvn clean package -DskipTests

# Image for running the application
FROM openjdk:17-jdk-alpine

WORKDIR /carts_service

COPY --from=builder /build/target/*.jar app.jar

ARG CARTS_SERVICE_PORT
ARG CARTS_DB_SERVICE_NAME
ARG CARTS_DB_SERVICE_PORT
ARG CARTS_DB_USER
ARG CARTS_DB_PASSWORD
ARG CARTS_DB_NAME

ENV CARTS_SERVICE_PORT=${CARTS_SERVICE_PORT}
ENV CARTS_DB_SERVICE_NAME=${CARTS_DB_SERVICE_NAME}
ENV CARTS_DB_SERVICE_PORT=${CARTS_DB_SERVICE_PORT}
ENV CARTS_DB_USER=${CARTS_DB_USER}
ENV CARTS_DB_PASSWORD=${CARTS_DB_PASSWORD}
ENV CARTS_DB_NAME=${CARTS_DB_NAME}

EXPOSE ${CARTS_SERVICE_PORT}

CMD [ "java", "-jar", "app.jar" ]