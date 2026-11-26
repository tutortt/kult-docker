# Etapa de construcción: compila el WAR ejecutable con el JDK 25
FROM eclipse-temurin:25-jdk-jammy AS build
WORKDIR /workspace

# Copiamos primero los archivos de configuración para aprovechar la caché de capas
COPY pom.xml mvnw ./
COPY .mvn/ .mvn/
RUN chmod +x ./mvnw && ./mvnw -B -DskipTests dependency:go-offline

COPY src/ src/
RUN ./mvnw -B -DskipTests clean package

# Etapa de ejecución: solo el JRE y el WAR generado
FROM eclipse-temurin:25-jre-jammy
WORKDIR /app

COPY --from=build /workspace/target/tienda-0.0.1-SNAPSHOT.war app.war

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.war"]
