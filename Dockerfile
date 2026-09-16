# ==========================================================
# Dockerfile Multi-Etapa para Aplicación Spring Boot 3 MVC
# ==========================================================

# Etapa 1: Compilación y empaquetado con Maven y Java 21
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

# Copiar descriptores para aprovechar cache de dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar codigo fuente y compilar artefacto JAR
COPY src ./src
RUN mvn clean package -DskipTests -B

# Etapa 2: Entorno de ejecucion liviano con JRE 21
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Crear usuario sin privilegios por seguridad
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copiar el ejecutable generado
COPY --from=builder /app/target/spring-boot-mvc-thymeleaf-*.jar app.jar

# Variables de entorno por defecto
ENV PORT=8080
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.port=${PORT} -jar app.jar"]
