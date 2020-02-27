FROM jvm:latest

# set environment
WORKDIR /tmp/work
EXPOSE 5005/tcp

COPY files/test-jar-with-dependencies.jar /tmp/work

RUN $JAVA_HOME/bin/java -version

CMD ["/tmp/work/jdk/bin/java","-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:5005", "-jar", "test-jar-with-dependencies.jar"]
