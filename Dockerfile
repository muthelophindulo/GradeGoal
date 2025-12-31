# Use verified Maven image with JDK 21
FROM maven:3.9.6-openjdk-21-slim AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Use verified OpenJDK 21 runtime
FROM openjdk:21-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]