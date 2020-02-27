#/bin/bash

export BUILD_DIR=/develop/jdk/build/linux-x86_64-server-fastdebug
export DOCKERFILE_DIR=docker/jvm
export IMAGE_NAME=jvm:latest

rm -fr $DOCKERFILE_DIR/files
mkdir $DOCKERFILE_DIR/files

tar zchvf ${DOCKERFILE_DIR}/files/developingjdk.tar.gz -C ${BUILD_DIR} jdk

docker rmi ${IMAGE_NAME}

pushd `pwd`
cd $DOCKERFILE_DIR
docker build -t ${IMAGE_NAME} .
popd