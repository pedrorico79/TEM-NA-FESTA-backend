# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /build

# Use faster Maven mirror (Aliyun para Brazil)
COPY settings.xml /usr/share/maven/conf/settings.xml

# Cache Maven dependencies (não recopia se não mudar pom.xml)
COPY pom.xml .
RUN mvn dependency:go-offline -B -q

# Build apenas quando código mudar
COPY src ./src
RUN mvn clean package -DskipTests -B -q

# Stage 2: Runtime (muito menor)
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Install wget for healthcheck and create a non-root user
RUN apk add --no-cache wget && \
    addgroup -g 1001 -S appuser && \
    adduser -u 1001 -S appuser -G appuser

# Copy JAR apenas
COPY --from=build /build/target/*.jar app.jar

USER appuser

EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
  CMD wget -q -O- http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]