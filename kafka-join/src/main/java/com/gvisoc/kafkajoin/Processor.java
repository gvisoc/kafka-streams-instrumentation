package com.gvisoc.kafkajoin;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
class Processor {
    private static final Logger logger = LoggerFactory.getLogger(Processor.class);
    @Autowired
    public void process(StreamsBuilder builder) {
        final Serde<String> stringSerde = Serdes.String();

        KStream<String, String> streamNumbers = builder.stream("numbers",
                Consumed.with(stringSerde, stringSerde));
        KStream<String, String> streamOddsEvens = builder.stream("oddsevens",
                Consumed.with(stringSerde, stringSerde));

        ValueJoiner<String, String, String> valueJoiner =
                (leftValue, rightValue) -> leftValue + " is " + rightValue + ".";

        streamNumbers.join(streamOddsEvens,
                valueJoiner,
                JoinWindows.of(Duration.ofSeconds(2))).to("result");
    }
}
