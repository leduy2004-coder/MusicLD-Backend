## syntax=docker/dockerfile:1
##Which "official Java image" ?
#FROM openjdk:24-oraclelinux8
##working directory
#WORKDIR /app
##copy from your Host(PC, laptop) to container
#COPY .mvn/ .mvn
#COPY mvnw pom.xml ./
##Run this inside the image
#RUN ./mvnw dependency:go-offline
#COPY src ./src
##run inside container
#CMD ["./mvnw", "spring-boot:run"]

FROM openjdk:17

ARG FILE_JAR=target/*.jar

ADD ${FILE_JAR} MusicLD.jar

ENTRYPOINT ["java","-jar","/MusicLD.jar"]

EXPOSE 8080