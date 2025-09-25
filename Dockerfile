# ---- Build stage ----
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY . .
# If you use the Maven wrapper in your project:
RUN chmod +x mvnw && ./mvnw -DskipTests package

# ---- Run stage ----
FROM eclipse-temurin:21-jre
WORKDIR /app
# copy the built jar
COPY --from=build /app/target/*.jar app.jar
# Render injects $PORT; Spring must listen on it
ENV SERVER_PORT=${PORT}
EXPOSE 8080
CMD ["sh", "-c", "java -Dserver.port=$PORT -jar app.jar"]
