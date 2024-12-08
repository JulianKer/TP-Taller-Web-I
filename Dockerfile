# Usa una imagen base de Maven con JDK 20
FROM maven:3.8.6-openjdk-20-slim AS build

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar los archivos del proyecto
COPY . /app

# Ejecutar Maven para compilar el proyecto
RUN mvn clean package -DskipTests

# Usar una imagen base de OpenJDK 20 para ejecutar la aplicación
FROM openjdk:20-jre-slim

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar el archivo WAR desde la imagen de build
COPY --from=build /app/target/tallerwebi-base-1.0-SNAPSHOT.war /app/app.war

# Exponer el puerto 8080 para que Render lo detecte
EXPOSE 8080

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "/app/app.war"]
