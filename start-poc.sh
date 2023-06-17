#!/bin/bash

docker ps 2>&1 > /dev/null
if [ $? -ne 0 ]; then
    echo -e "Error --docker doesn't appear to be running. Please start docker"
fi

# kafka & dashboard
docker compose up -d

#topics
./create-topics.sh

# build
cd kafka-producer
./mvnw package -T4
cd ../kafka-join
./mvnw package -T4
cd ../kafka-consumer
./mvnw package -T4

# run
cd ../kafka-producer
./run-otel.sh &
cd ../kafka-join
./run-otel.sh &
cd ../kafka-consumer
./run-otel.sh &
cd ..
echo -e "Open Jaeger UI at http://localhost:16686¨