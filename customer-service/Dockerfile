FROM gradle:jdk17 as gradle
WORKDIR /app
COPY . /app
RUN gradle clean
#RUN gradle test -info
RUN gradle bootJar

FROM openjdk:17-alpine
WORKDIR /app
COPY --from=gradle /app/build/libs/*SNAPSHOT.jar /app.jar
EXPOSE 8084
CMD ["java", "-jar", "/app.jar"]
