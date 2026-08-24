# ==========================================
# Stage 1 - Build
# ==========================================
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Arquivos necessários para baixar dependências
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

RUN chmod +x mvnw

# Aproveita o cache das dependências
RUN ./mvnw dependency:go-offline -B

# Código fonte
COPY src ./src

# Build
RUN ./mvnw clean package -DskipTests


# ==========================================
# Stage 2 - Runtime
# ==========================================
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
