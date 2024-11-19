# stage 1: build the application
FROM maven:3.9.4-eclipse-temurin-17 AS builder

# set the working directory
WORKDIR /app

# copy the maven project files
COPY pom.xml ./
COPY src ./src

# build the application
RUN mvn clean package -DskipTests

# stage 2: run the application
FROM openjdk:17-jdk-slim

# working directory in the container
WORKDIR /app

# copy the maven build output to the container
COPY --from=builder /app/target/backend.jar app.jar

# expose the port spring boot runs on
EXPOSE 8080

# run the spring boot application
ENTRYPOINT ["java", "-jar", "app.jar"]