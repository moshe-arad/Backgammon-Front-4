package org.moshe.arad.kafka;

import java.util.UUID;
import java.util.concurrent.PriorityBlockingQueue;

import org.moshe.arad.kafka.events.BackgammonEvent;
import org.springframework.stereotype.Component;

@Component
public class ConsumerToFrontServiceQueue {

	private static final int QUEUE_SIZE = 100000;
	private static final int SLEEP = 100;
	
	private PriorityBlockingQueue<BackgammonEvent> eventsQueue = new PriorityBlockingQueue<>(QUEUE_SIZE, 
			(BackgammonEvent o1, BackgammonEvent o2) -> {return o1.getArrived().compareTo(o2.getArrived());});
	
	public PriorityBlockingQueue<BackgammonEvent> getEventsQueue() {
		return eventsQueue;
	}
	
//	public BackgammonEvent takeBackgammonEventByUUID(UUID uuid){
//		if(eventsQueue.stream().filter((BackgammonEvent event) -> event.equals(uuid)).count() == 0){
//			try {
//				Thread.sleep(SLEEP);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//		
//		if(eventsQueue.stream().filter((BackgammonEvent event) -> event.equals(uuid)).count() == 0){
//			return null;
//		}
//		else return eventsQueue. stream().filter((BackgammonEvent event) -> event.equals(uuid)).count() == 0
//	}
}
