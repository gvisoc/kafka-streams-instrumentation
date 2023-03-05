# Instrumentation of Kafka Streams
This repo is a small PoC to see in Jaeger how the Open Telemetry traces are propagated and represented when joining 2 streams.

## Application
The end to end application consists in three Spring Boot applications. These applications send data to each other through a local Kafka cluster (on Docker), and they  instrumented with Open Telemetry.
1. A Kafka producer creating 2 topics, one with a number and another one with whether the number is odd or even. 
    - The events are created every 5 seconds.
    - The topics are `numbers` and `oddsevens`.
    - All values are serialised as `String`.
    - Both events share the same key, an UUID serialised as a `String`.
2. An intermediate application joining those 2 topics into 1, using Kafka Streams.
    - The resulting topic is `result`. The value is a concatenation of the two values (`String`), e.g., `23 is ODD`.
3. A consumer, consuming and logging to standard output every combined event.

The distributed trace information is represented in Jaeger. For this, the applications' instrumentation is done with the Open Telemetry agent (see [Automatic Instrumentation](https://opentelemetry.io/docs/instrumentation/java/automatic/)), configured to use the Jaeger exporter. 

An example of the Open Telemetry Agent usage is in the `run-otel.sh` script that launches the `kafka-join` intermediate Spring Boot Application:
```bash
#!/bin/bash
java -javaagent:../opentelemetry-javaagent.jar \
      -Dotel.service.name=kafka-streams-join-poc \
      -Dotel.traces.exporter=jaeger \
      -Dotel.metrics.exporter=none \
      -Dotel.instrumentation.messaging.experimental.receive-telemetry.enabled=true \
      -jar ./target/kafka-join-0.0.1-SNAPSHOT.jar
```

## Run the example
To run the example, proceed in this order:
1. Create the Kafka Cluster and the Jaeger server by running `docker compose up -d` on this directory.
2. Create the topics by running `./create-topics.sh`.
3. Compile and package the three applications, running `mvn package` inside each subdirectory `kafka-{producer/consumer/join}`
4. Run each of the applications bu running `otel.sh` under the three applications: 
   - `kafka-consumer`
   - `kafka-join`
   - `kafka-producer`

It is easy to verify the applications works in the logs --there are currently no tests in this repository. 

The Jaeger UI is accessible at [http://localhost:16686](http://localhost:16686).

## Conclusions
Stateful operations on Kafka Streams, like joins, are not propagating all needed headers through RocksDB. By running this example we can see that the end to end observability is broken with the current scenario. Stateless operations or 1:1 processes are presumably OK.

See also: 
- [KAFKA-7718](https://issues.apache.org/jira/browse/KAFKA-7718) on the Apache Software Foundation Jira.
- [Trace/span lost during stateful processing in Kafka streams #6569](https://github.com/open-telemetry/opentelemetry-java-instrumentation/issues/6569) on Open Telemetry's GitHub
- [Integrating Apache Kafka Clients with CNCF Jaeger at Funding Circle Using OpenTelemetry](https://www.confluent.io/blog/integrate-kafka-and-jaeger-for-distributed-tracing-and-monitoring/)

## Caveats
There are no unit tests in these repositories.