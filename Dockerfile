FROM openjdk:17.0.2-oracle
ARG JAR_FILE=target/*.jar
COPY target/demo7-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 5000
ENV JAVA_TOOL_OPTIONS "-Dcom.sun.management.jmxremote.ssl=false \
 -Dcom.sun.management.jmxremote.authenticate=false \
 -Dcom.sun.management.jmxremote.port=5000 \
 -Dcom.sun.management.jmxremote.rmi.port=5000 \
 -Dcom.sun.management.jmxremote.host=0.0.0.0 \
 -Djava.rmi.server.hostname=0.0.0.0"
ENTRYPOINT ["java","-jar","/app.jar", "--server.port=8080"]