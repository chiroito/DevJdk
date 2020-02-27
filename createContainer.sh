#/bin/bash

export DOCKERFILE_DIR=docker/sleep
export CONTAINER_NAME=SleepContainer
export IMAGE_NAME=sleep:latest

rm -fr $DOCKERFILE_DIR/files
mkdir $DOCKERFILE_DIR/files

cp target/test-jar-with-dependencies.jar $DOCKERFILE_DIR/files/

docker rmi ${IMAGE_NAME}

pushd `pwd`
cd $DOCKERFILE_DIR
docker build -t ${IMAGE_NAME} .
popd

docker stop ${CONTAINER_NAME}
docker rm ${CONTAINER_NAME}
docker run -it --name ${CONTAINER_NAME} ${IMAGE_NAME}
