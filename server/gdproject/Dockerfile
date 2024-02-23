FROM gradle:latest as build
COPY --chown=gradle:gradle . .
RUN ./gradlew build --stacktrace


FROM eclipse-temurin:latest
EXPOSE 8080
ARG JAR_FILE=build/libs/*.jar
COPY --from=build ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]