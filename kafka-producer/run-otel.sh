#!/bin/bash
java -javaagent:../opentelemetry-javaagent.jar \
      -Dotel.service.name=kafka-streams-join-poc \
      -Dotel.traces.exporter=jaeger \
      -Dotel.metrics.exporter=none \
      -jar ./target/kafka-producer-0.0.1-SNAPSHOT.jar

# -Dotel.instrumentation.messaging.experimental.receive-telemetry.enabled=true \
