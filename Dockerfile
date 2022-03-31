FROM openjdk:17.0.2-oracle
ARG JAR_FILE=target/*.jar
COPY target/demo5-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar", "--server.port=8081"]