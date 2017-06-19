package org.moshe.arad.kafka.producers.events;

import java.util.UUID;

import org.moshe.arad.kafka.events.BackgammonEvent;
import org.moshe.arad.kafka.producers.ISimpleProducer;

public interface ISimpleEventProducer <T extends BackgammonEvent> extends ISimpleProducer{

	public void sendKafkaMessage(T event);
	 
	default public UUID generateUUID(){
		return UUID.randomUUID();
	}
}
