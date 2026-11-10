FROM maven:3.9.11-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml .
COPY src ./src
RUN mvn --batch-mode --no-transfer-progress clean verify

FROM eclipse-temurin:21-jre
WORKDIR /app
RUN useradd --system --uid 10001 --user-group appuser \
    && mkdir -p /data \
    && chown -R appuser:appuser /app /data
COPY --from=build --chown=appuser:appuser \
    /workspace/target/nova-roupa-api-0.1.0-SNAPSHOT.jar /app/app.jar
USER appuser
EXPOSE 8080
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-jar", "/app/app.jar"]
