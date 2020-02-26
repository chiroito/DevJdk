FROM ubuntu:latest

ADD developingjdk.tar.gz /tmp/work/
COPY target/test-jar-with-dependencies.jar /tmp/work

WORKDIR /tmp/work

RUN ls -l 
RUN ls -l jdk/lib/jvm.cfg
RUN jdk/bin/java -version 

CMD ["/tmp/work/jdk/bin/java","-jar", "test-jar-with-dependencies.jar"] 
