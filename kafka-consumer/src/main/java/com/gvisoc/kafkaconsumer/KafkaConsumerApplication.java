package com.gvisoc.kafkaconsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;

@SpringBootApplication
public class KafkaConsumerApplication {
	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(KafkaConsumerApplication.class);
		application.setWebApplicationType(WebApplicationType.NONE);
		application.run(args);
	}

	@Bean
	public CommandLineRunner CommandLineRunnerBean() {
		return (args) -> {
			MessageListenerContainer listenerContainer =
					kafkaListenerEndpointRegistry.getListenerContainer("myConsumer");
			listenerContainer.start();
		};
	}


	@Autowired
	private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

}
