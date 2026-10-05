FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY gradle ./gradle
COPY gradlew gradlew.bat build.gradle settings.gradle ./
COPY src ./src

RUN chmod +x gradlew && ./gradlew build -x test --info

FROM eclipse-temurin:21-jre

WORKDIR /app

RUN apt-get update && apt-get install -y tzdata && rm -rf /var/lib/apt/lists/*

COPY --from=0 /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-Duser.timezone=America/Sao_Paulo", "-jar", "app.jar"]

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
