package com.gvisoc.kafkaproducer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.UUID;


@SpringBootApplication
public class KafkaProducerApplication {

	private static final String ODD = "ODD";
	private static final String EVEN = "EVEN";

	private final NumberProducer numberProducer;
	private final OddsEvensProducer oddsEvensProducer;

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(KafkaProducerApplication.class);
		application.setWebApplicationType(WebApplicationType.NONE);
		application.run(args);
	}

	@Bean
	public CommandLineRunner CommandLineRunnerBean() {
		return (args) -> {
			int value = 0;
			UUID id = null;

			while(true) {
				value = (int) Math.round(100*Math.random());
				id = UUID.randomUUID();
				this.numberProducer.sendMessage(id.toString(), String.valueOf(value));
				this.oddsEvensProducer.sendMessage(id.toString(), (value % 2 == 0)?EVEN:ODD );
				Thread.sleep(5000);
			}
		};
	}

	@Autowired
	KafkaProducerApplication(OddsEvensProducer oddsEvensProducer, NumberProducer numberProducer) {
		this.oddsEvensProducer = oddsEvensProducer;
		this.numberProducer = numberProducer;
	}
}