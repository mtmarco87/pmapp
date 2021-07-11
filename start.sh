#!/bin/bash


# Backend image preparation

printf "1) Preparing BE docker image...\n\n"

cd pmapp-be
./mvnw package
docker build -t ricardo/pmapp-be .

printf "3) Running Docker Compose ...\n\n"

cd ..
docker-compose up
