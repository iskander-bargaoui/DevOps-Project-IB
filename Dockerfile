## Stage 1: Building Application Back
FROM maven:3.8.3-openjdk-8 AS builder
WORKDIR /app
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn dependency:go-offline
COPY src/ src/
RUN --mount=type=cache,target=/root/.m2 mvn package

## Stage 2: Create the runtime container
FROM openjdk:8-jre-slim
EXPOSE 8082
# Install curl in the container
RUN apt-get update && apt-get install -y curl
# Download the .jar file from Nexus and copy it to the container
ARG NEXUS_URL="http://192.168.126.175:8081/repository/maven-releases/"
ARG ARTIFACT_PATH="tn/esprit/DevOps_Project/1.0/DevOps_Project-1.0.jar"
RUN curl -o /DevOps_Project-1.0.jar ${NEXUS_URL}${ARTIFACT_PATH}
ENV JAVA_OPTS="-Dlogging.level.org.springframework.security=DEBUG -Djdk.tls.client.protocols=TLSv1.2"
ENTRYPOINT ["java", "-jar", "/DevOps_Project-1.0.jar"]
