FROM gcr.io/distroless/java17-debian11:debug

# Add to the container
COPY ./build/libs/app.jar /app.jar

# Add environment
ENV LC_ALL=C.UTF-8

ENV DATABASE_HOST=database
ENV DATABASE_PORT=5432
ENV DATABASE_USER=root
ENV DATABASE_PASSWORD=root
ENV SPRING_LIQUIBASE_ENABLED=false

# console
ENV API_DOC_ENABLE=false

# Run the JAR file
ENTRYPOINT ["java", "-jar", "/app.jar"]