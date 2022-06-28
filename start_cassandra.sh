#!/bin/bash

# Source: https://cassandra.apache.org/_/quickstart.html
# STEP 1: GET CASSANDRA USING DOCKER
#	$ docker pull cassandra:latest
#	
# STEP 2: START CASSANDRA
#	A Docker network allows us to access the container’s ports without exposing them on the host.
#
# 	$ docker network create cassandra
#	$ docker run --rm -d --name cassandra --hostname cassandra --network cassandra cassandra
#	$ docker run -p 9042:9042 --rm --name cassandra -d cassandra:latest

docker pull cassandra:latest
docker network create cassandra
docker run --rm -d --name cassandra  --network cassandra \
-p 7000:7000 \
-p 7001:7001 \
-p 7199:7199 \
-p 9042:9042 \
-p 9160:9160 \
--hostname cassandra \
cassandra 