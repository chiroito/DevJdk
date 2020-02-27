FROM jvm:latest

# set environment
WORKDIR /tmp/work

RUN $JAVA_HOME/bin/java -version

CMD ["/tmp/work/jdk/bin/jcmd","-l"]
