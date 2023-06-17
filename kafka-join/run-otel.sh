#!/bin/bash
java -javaagent:../opentelemetry-javaagent.jar \
      -Dotel.service.name=kafka-join \
      -Dotel.traces.exporter=jaeger \
      -Dotel.metrics.exporter=none \
      -Dotel.instrumentation.messaging.experimental.receive-telemetry.enabled=true \
      -Dotel.propagators=tracecontext,baggage,jaeger \
      -jar ./target/kafka-join-0.0.1-SNAPSHOT.jar
