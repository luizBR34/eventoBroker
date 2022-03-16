package com.eventoBroker.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.eventoBroker")
public class EventoBrokerApplication {

/* INSTRUCTIONS FOR BUILD USING DOCKERFILE

	docker build -t luizpovoa/eventobroker:0.0.1-SNAPSHOT .
	docker run -p 8888:8888 -d -e RABBITMQ_HOST=172.18.0.3 -e RABBITMQ_PORT=5672 -e RABBITMQ_USERNAME=eventoapp -e RABBITMQ_PASSWORD=segredo -e EVENTO_APP_QUEUE=eventoapp-integration-queue -e RESPONSE_EXCHANGE=eventoapp.integration.exchange -e EVENTO_WS_URI=http://172.18.0.11:9090 --network eventoapp-network --name EventoBroker luizpovoa/eventobroker:0.0.1-SNAPSHOT
*/


	public static void main(String[] args) {
		SpringApplication.run(EventoBrokerApplication.class, args);
	}

}
