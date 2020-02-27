#/bin/bash

export DOCKERFILE_DIR=docker/jcmd
export CONTAINER_NAME=JcmdContainer
export IMAGE_NAME=jcmd:latest

docker rmi ${IMAGE_NAME}

pushd `pwd`
cd $DOCKERFILE_DIR
docker build -t ${IMAGE_NAME} .
popd

docker stop ${CONTAINER_NAME}
docker rm ${CONTAINER_NAME}
docker run -it --name ${CONTAINER_NAME} ${IMAGE_NAME}
