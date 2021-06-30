package com.eventoBroker.services;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

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
import org.springframework.web.util.UriComponentsBuilder;

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

	@Override
	public void onMessage(Message message) {
		
		if (message.getMessageProperties().getHeader("operation").equals("event")) {
			
			Event event = null;
			
			try {
				event = mapper.readValue(message.getBody(), Event.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			saveEvent(event);
			
		} else if (message.getMessageProperties().getHeader("operation").equals("guest")) {
			
			Guest guest = null;
			long eventCode = message.getMessageProperties().getHeader("eventCode");
			
			try {
				guest = mapper.readValue(message.getBody(), Guest.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			saveGuest(eventCode, guest);
			
		} else { }
		
	}

	@Override
	public void saveEvent(Event event) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Event> request = new HttpEntity<>(event, headers);
		String path = endpointURI + "/saveEvent";
		ResponseEntity<Event> responseEntity = restTemplate.exchange(path, HttpMethod.POST, request, Event.class);
		
		if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
			log.info("BrokerListenerServiceImpl:saveEvent() - Event sent to EventoWS API with successfull!");
		} else {
			log.error("Error when sent Event to EventWS API!");
		}
	}

	@Override
	public void saveGuest(long eventCode, Guest guest) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Guest> request = new HttpEntity<>(guest, headers);
		String path = endpointURI + "/saveGuest/{eventCode}";

		Map<String, Long> variables = new HashMap<String, Long>();
		variables.put("eventCode", eventCode);
		
		URI uri = UriComponentsBuilder.fromUriString(path)
		        .buildAndExpand(variables)
		        .toUri();
		
		ResponseEntity<Guest> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, request, Guest.class);
		
		if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
			log.info("BrokerListenerServiceImpl:saveGuest() - Guest sent to EventoWS API with successfull!");
		} else {
			log.error("Error when sent Guest to EventWS API!");
		}
		
	}
}
