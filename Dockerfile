# Stage 1: Build với Maven và Amazon Corretto 21
FROM maven:3.9.8-amazoncorretto-21 AS build

# Copy source code và pom.xml vào /app folder
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build source code với Maven
RUN mvn package -DskipTests

# Stage 2: Chạy ứng dụng với OpenJDK 22
FROM openjdk:22-jdk

# Set working folder to /app và copy compiled file từ bước build
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Chạy ứng dụng
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

# Expose port 8080
EXPOSE 8080
