package com.eventoBroker.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.eventoBroker")
public class EventoBrokerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventoBrokerApplication.class, args);
	}

}
