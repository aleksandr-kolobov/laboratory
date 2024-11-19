#FROM gradle:jdk17 as builder
#WORKDIR /app
#COPY . /app
#RUN gradle clean build
#
#FROM openjdk:17-alpine
#WORKDIR /app
#COPY --from=builder /app/build/libs/*SNAPSHOT.jar /app/app.jar
#EXPOSE 8761
#CMD [ "java", "-jar", "/app/app.jar"]

FROM openjdk:17-oracle

WORKDIR /app

COPY build/libs/discovery-service-0.0.1-SNAPSHOT.jar app.jar

CMD ["java", "-jar", "app.jar"]
