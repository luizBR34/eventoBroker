package com.eventoBroker.services;

import org.springframework.amqp.core.MessageListener;

import com.eventoApp.models.Event;
import com.eventoApp.models.Guest;

public interface BrokerListenerService extends MessageListener {
	
	public void saveEvent(Event event);
	public void saveGuest(long guestCode, Guest guest);

}
