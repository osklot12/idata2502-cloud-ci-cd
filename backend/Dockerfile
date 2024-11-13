FROM openjdk:17-jdk-slim

# setting environment variables
ENV SPRING_PROFILES_ACTIVE=prod

# working directory in the container
WORKDIR /app

# copy the maven build output to the container
COPY target/backend.jar app.jar

# expose the port spring boot runs on
EXPOSE 8080

# run the spring boot application
ENTRYPOINT ["java", "-jar", "app.jar"]