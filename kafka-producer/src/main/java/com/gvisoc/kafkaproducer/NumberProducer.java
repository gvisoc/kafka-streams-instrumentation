package com.gvisoc.kafkaproducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.kafka.support.SendResult;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

@Service
public class NumberProducer {

    private static final Logger logger = LoggerFactory.getLogger(OddsEvensProducer.class);
    private static final String TOPIC = "numbers";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String key, String value) {
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(TOPIC, key, value);

        future.whenComplete((stringStringSendResult, throwable) -> {
            if (throwable == null) {
                logger.info(String.format("Produced event to topic %s: key = %-10s value = %s", TOPIC, key, value));
            }
            else {
                logger.error("Received throwable with message: {}. Throwing RuntimeException.",
                        throwable.getMessage());
                throw new RuntimeException(throwable);
            }
        });
    }
}