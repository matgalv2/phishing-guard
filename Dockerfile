FROM gradle:8.14.3-jdk21 AS build
WORKDIR /workspace

COPY build.gradle settings.gradle ./
COPY gradle gradle
RUN gradle build --no-daemon || return 0

COPY . .
RUN gradle clean bootJar --no-daemon

FROM eclipse-temurin:21-jre
ENV JAVA_OPTS="-Xms1024m -Xmx2048m" \
    DATABASE_URL=localhost \
    DATABASE_USERNAME=user \
    DATABASE_PASSWORD=pass \
    CACHE_HOST=cache \
    CACHE_PORT=6379 \
    GOOGLE_WEBRISK_TOKEN=token
WORKDIR /app
COPY --from=build /workspace/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
