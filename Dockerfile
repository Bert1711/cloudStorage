FROM openjdk:11
EXPOSE 8081
ADD draftCloudStorage-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]