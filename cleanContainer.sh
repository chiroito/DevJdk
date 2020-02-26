#/bin/bash

docker ps -a | awk 'NR>1 {print $1}' | xargs -I {} docker rm {}
docker images | awk 'NR>1 {print $3}' | xargs -I {} docker rmi {}

