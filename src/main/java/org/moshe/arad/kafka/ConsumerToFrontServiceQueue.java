package org.moshe.arad.kafka;

import java.util.concurrent.PriorityBlockingQueue;

import org.moshe.arad.kafka.events.BackgammonEvent;
import org.springframework.stereotype.Component;

@Component
public class ConsumerToFrontServiceQueue {

	private static final int QUEUE_SIZE = 100000;
	
	private PriorityBlockingQueue<BackgammonEvent> eventsQueue = new PriorityBlockingQueue<>(QUEUE_SIZE, 
			(BackgammonEvent o1, BackgammonEvent o2) -> {return o1.getArrived().compareTo(o2.getArrived());});
	
	public PriorityBlockingQueue<BackgammonEvent> getEventsQueue() {
		return eventsQueue;
	}
}
