FROM openjdk:8-jdk-alpine

COPY build/libs/scramble.jar /root/scramble.jar

RUN apk add --no-cache ttf-freefont

CMD ["java", "-jar", "/root/scramble.jar"]