FROM bellsoft/liberica-runtime-container:jdk-25-slim-musl AS builder
WORKDIR /application
ARG JAR_FILE=build/libs/redist-springboot-sample-1.2.jar
COPY ${JAR_FILE} app.jar
# Use the layertools to split the fat jar into logical layers
RUN java -Djarmode=layertools -jar app.jar extract

# Stage 2: Final Runtime Image
FROM bellsoft/liberica-runtime-container:jdk-25-slim-musl
LABEL author="Gerrits Marc"

# Security: Non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
WORKDIR /application

# Copy the layers from the builder stage
# This ensures that if only your code changes, Docker only replaces the 'application' layer (~1MB)
COPY --from=builder /application/dependencies/ ./
COPY --from=builder /application/spring-boot-loader/ ./
COPY --from=builder /application/snapshot-dependencies/ ./
COPY --from=builder /application/application/ ./

# Java 25 High-Performance Flags:
# - TieredStopAtLevel=1: Fastest JIT startup for containers
# - Virtual Threads: Enabled for Java 25 performance
# - CDS: Uses the generated class data archive if present
ENV JAVA_OPTS="-XX:TieredStopAtLevel=1 \
               -Dspring.threads.virtual.enabled=true \
               -Dspring.main.lazy-initialization=true \
               -Dspring.data.jpa.repositories.bootstrap-mode=deferred"

# Using JarLauncher is faster than 'java -jar' because it bypasses JAR indexing
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS org.springframework.boot.loader.launch.JarLauncher"]