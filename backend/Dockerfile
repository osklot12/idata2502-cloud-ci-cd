FROM openjdk:17-jdk-slim

# working directory in the container
WORKDIR /app

# copy the maven build output to the container
COPY target/backend.jar /app/backend.jar

# expose the port spring boot runs on
EXPOSE 8080

# run the spring boot application
ENTRYPOINT ["java", "-jar", "/app/backend.jar"]