#/bin/bash

tar zchvf developingjdk.tar.gz -C /develop/jdk/build/linux-x86_64-server-fastdebug jdk
docker build . -t sleep
docker run -it --name SleepContainer sleep
