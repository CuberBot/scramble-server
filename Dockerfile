FROM openjdk:8-jdk-alpine

COPY build/libs/scramble.jar /root/scramble.jar

CMD ["java", "-jar", "/root/scramble.jar"]