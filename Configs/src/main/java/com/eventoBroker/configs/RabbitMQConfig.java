package com.eventoBroker.configs;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.eventoBroker.services.BrokerListenerService;

@Configuration
@EnableRabbit
public class RabbitMQConfig {
	
	@Value("${evento.broker.queue}")
	String queueName;
	
	@Value("${evento.broker.exchange}")
	String exchangeName;
	
	@Value("${spring.rabbitmq.host}")
	String rabbitHost;

	@Value("${spring.rabbitmq.username}")
	String username;

	@Value("${spring.rabbitmq.password}")
	private String password;
	
	@Autowired
	private BrokerListenerService listenerService;

	@Bean
	Queue queue() {
		return new Queue(queueName, true, false, false);
	}
	
	
	@Bean
	public TopicExchange exchange() {
		TopicExchange exchange = new TopicExchange(exchangeName, true, false);
		return exchange;
	}

    @Bean
    public Binding makeDataBinding() {
        return BindingBuilder.bind(queue()).to(exchange()).with("routingKey_eventoapp");
    }
	
	
	@Bean
	public ConnectionFactory connectionFactory() {
	    CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
	    connectionFactory.setHost(rabbitHost);
	    connectionFactory.setUsername(username);
	    connectionFactory.setPassword(password);
	    return connectionFactory;
	}
	
	
    //create MessageListenerContainer using default connection factory
	@Bean
	MessageListenerContainer messageListenerContainer() {
		SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
		simpleMessageListenerContainer.setMaxConcurrentConsumers(5);
		simpleMessageListenerContainer.setConnectionFactory(connectionFactory());
		simpleMessageListenerContainer.setQueues(queue());
		simpleMessageListenerContainer.setMessageListener(listenerService);
		return simpleMessageListenerContainer;
	}
}
