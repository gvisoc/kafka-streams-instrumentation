#!/bin/bash
java -javaagent:../opentelemetry-javaagent.jar \
      -Dotel.service.name=kafka-consumer \
      -Dotel.traces.exporter=jaeger \
      -Dotel.metrics.exporter=none \
      -Dotel.instrumentation.messaging.experimental.receive-telemetry.enabled=true \
      -Dotel.propagators=tracecontext,baggage,jaeger \
      -jar ./target/kafka-consumer-0.0.1-SNAPSHOT.jar
