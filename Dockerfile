FROM amazoncorretto:11-alpine-jdk
MAINTAINER JulianKer
COPY target/tallerwebi-base-1.0-SNAPSHOT.war /app/app.war

# Exponer el puerto 8080 para que Render lo detecte
EXPOSE 8080

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "/app/app.war"]
