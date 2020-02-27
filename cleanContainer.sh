#/bin/bash

#docker ps -a | awk 'NR>1 {print $1}' | xargs -I {} docker rm {}
#docker images | awk 'NR>1 {print $3}' | xargs -I {} docker rmi {}

for CONTAINER_NAME in SleepContainer JcmdContainer; do
  docker stop ${CONTAINER_NAME}
  docker rm ${CONTAINER_NAME}
done

for IMAGE_NAME in sleep jcmd jvm; do
  docker rmi $IMAGE_NAME:latest
done