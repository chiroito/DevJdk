FROM jvm:latest

# set environment
WORKDIR /tmp/work

COPY files/test-jar-with-dependencies.jar /tmp/work

RUN $JAVA_HOME/bin/java -version

CMD ["/tmp/work/jdk/bin/java","-jar", "test-jar-with-dependencies.jar"]
