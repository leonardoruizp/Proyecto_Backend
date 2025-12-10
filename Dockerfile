# Etapa 1: construir el JAR
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY consultoriodental/pom.xml .
RUN mvn -q dependency:go-offline
COPY consultoriodental/src ./src
RUN mvn -q -DskipTests clean package

# Etapa 2: imagen final
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copiar el JAR generado en la etapa anterior
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
