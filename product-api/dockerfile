FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} product-api-0.0.1-SNAPSHOT.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "product-api-0.0.1-SNAPSHOT.jar"]
