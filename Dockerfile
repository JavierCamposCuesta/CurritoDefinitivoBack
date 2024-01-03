FROM openjdk:18-jdk-alpine
COPY target/CurritosAPI-0.0.1-SNAPSHOT.jar java-app.jar
ENTRYPOINT ["java", "-jar", "java-app.jar"]