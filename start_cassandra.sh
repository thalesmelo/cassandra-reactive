#!/bin/bash

docker run -p 9042:9042 --rm --name cassandra -d cassandra:3.11