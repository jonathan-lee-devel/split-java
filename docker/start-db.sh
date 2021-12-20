#!/bin/bash

docker-compose -f mongo-docker-compose.yml up -d

sleep 5

docker exec mongo1 rs-init.sh
