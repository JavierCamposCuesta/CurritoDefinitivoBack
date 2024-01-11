FROM adoptopenjdk:11-jre-hotspot
COPY "target/CurritosAPI-0.0.1-SNAPSHOT.jar" "java-app.jar"
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "java-app.jar"]
