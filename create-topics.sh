#!/bin/bash
docker compose exec broker kafka-topics --create --topic numbers --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1
docker compose exec broker kafka-topics --create --topic oddsevens --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1
docker compose exec broker kafka-topics --create --topic result --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1
