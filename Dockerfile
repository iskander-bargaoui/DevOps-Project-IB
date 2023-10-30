##Stage 1: Building Application Back
FROM maven:3.8.3-openjdk-8 AS builder
WORKDIR /app
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn dependency:go-offline
COPY src/ src/
RUN --mount=type=cache,target=/root/.m2 mvn package

##Stage 2: Create the runtime container
FROM openjdk:8-jre-slim
EXPOSE 8082
COPY --from=builder /app/target/DevOps_Project-1.0.jar /DevOps_Project-1.0.jar
ENV JAVA_OPTS="-Dlogging.level.org.springframework.security=DEBUG -Djdk.tls.client.protocols=TLSv1.2"
ENTRYPOINT ["java", "-jar", "/DevOps_Project-1.0.jar"]
