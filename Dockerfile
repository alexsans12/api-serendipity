FROM maven:3.9.3 AS build
WORKDIR /app
ARG CONTAINER_PORT
COPY pom.xml /app
RUN mvn dependency:resolve
COPY . /app
RUN mvn clean
RUN mvn package -DskipTests -X

FROM openjdk:17
COPY --from=build /app/target/*.jar app.jar
EXPOSE ${CONTAINER_PORT}
CMD ["java", "-jar", "app.jar"]

LABEL authors="Alexsans"
LABEL version="1.2.3"
LABEL description="This is the base docker image for the spring boot application"