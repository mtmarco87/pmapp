#!/bin/bash

printf "1) Preparing BE docker image...\n\n"

cd pmapp-be
./mvnw package
docker build -t ricardo/pmapp-be .

printf "\n2) Preparing FE docker image...\n\n"

cd ../pmapp-fe
yarn install
yarn test --watchAll=false
yarn build
docker build -t ricardo/pmapp-fe .

printf "\n3) Running Docker Compose ...\n\n"

cd ..
docker-compose up -d

# Starts a browser on the FE

#sleep 10 

echo "Waiting for nginx/pmapp-fe to launch on 3000..."

while ! nc -z localhost 3000; do   
  sleep 0.1 # wait for 1/10 of the second before check again
done

if command -v xdg-open &> /dev/null
then
    xdg-open "http://localhost:3000"
    exit
fi

if command -v sensible-browser &> /dev/null
then
    sensible-browser "http://localhost:3000"
    exit
fi

if command -v open &> /dev/null
then
    open "http://localhost:3000"
    exit
fi

echo "PmApp launched!"
