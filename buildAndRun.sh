#!/bin/sh

# exit on any of building commands failure
set -e

mvn clean install

sudo docker build -t org.mv/po .

# ignore the following commands failures (if container is not running yet for instance)
set +e
sudo docker kill /po
sudo docker rm /po

sudo docker run -d -p 8080:8080 -p 4848:4848 --name po org.mv/po 
