FROM gradle:jdk17 as builder
WORKDIR /app
COPY . /app
RUN gradle clean build

FROM openjdk:17-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/*SNAPSHOT.jar /app/app.jar
EXPOSE 8085
CMD [ "java", "-jar", "/app/app.jar"]
