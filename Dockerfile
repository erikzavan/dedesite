# ---- Build ----
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY . .
RUN chmod +x mvnw && ./mvnw -DskipTests package

# ---- Run ----
FROM eclipse-temurin:24-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENV SERVER_PORT=${PORT}
EXPOSE 8080
CMD ["sh","-c","java -Dserver.port=$PORT -jar app.jar"]
