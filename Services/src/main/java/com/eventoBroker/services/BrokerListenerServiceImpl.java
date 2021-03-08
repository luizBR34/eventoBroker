package com.eventoBroker.services;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.eventoApp.models.Event;
import com.eventoApp.models.Guest;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BrokerListenerServiceImpl implements BrokerListenerService {
	
    @Value(value = "${eventows.endpoint.uri}")
    private String endpointURI;
    
    private final Logger log = LoggerFactory.getLogger(BrokerListenerServiceImpl.class);
	
	ObjectMapper mapper = new ObjectMapper();
	RestTemplate restTemplate = new RestTemplate();
	Event event = null;

	@Override
	public void onMessage(Message message) {

		try {
			event = mapper.readValue(message.getBody(), Event.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		saveEvent(event);
	}

	@Override
	public void saveEvent(Event event) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Event> request = new HttpEntity<>(event, headers);
		ResponseEntity<Event> responseEntity = restTemplate.exchange(endpointURI, HttpMethod.POST, request, Event.class);
		
		if (responseEntity.getStatusCode().equals(HttpStatus.CREATED)) {
			log.info("BrokerListenerServiceImpl:saveEvent() - Event sent to EventoWS API with successfull!");
		} else {
			log.error("Error when sent Event to EventWS API!");
		}
	}

	@Override
	public void saveGuest(long guestCode, Guest guest) {
		// TODO Auto-generated method stub
		
	}
}
