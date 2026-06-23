# ESTÁGIO 1: Build (Compilação)
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copia o pom.xml primeiro para aproveitar o cache do Docker nas dependências
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o código-fonte e faz o build (pulando testes para ser mais rápido)
COPY src ./src
RUN mvn clean package -DskipTests

# ESTÁGIO 2: Run (Execução)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copia o .jar gerado no estágio anterior
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta padrão do Spring Boot
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]