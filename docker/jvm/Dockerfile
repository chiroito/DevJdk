FROM ubuntu:latest

# set environment information
WORKDIR /tmp/work
ENV JAVA_HOME /tmp/work/jdk

# Extrat
ADD files/developingjdk.tar.gz /tmp/work/

# try to confirm success
RUN $JAVA_HOME/bin/java -version

CMD ["$JAVA_HOME/bin/java","-version"]
