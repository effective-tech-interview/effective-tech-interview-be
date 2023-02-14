FROM amazoncorretto:17-alpine-jdk

WORKDIR /app

COPY /build/libs/teamf-0.0.1-SNAPSHOT.jar /app/teamf-0.0.1-SNAPSHOT.jar

ENV encrypt_key=encrypt_key

ENV gpt_key1=gpt_key1

ENV PORT=8080

EXPOSE $PORT

CMD ["java","-Dspring.profiles.active=local", "-jar", "/app/teamf-0.0.1-SNAPSHOT.jar"]