package org.moshe.arad.kafka;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.moshe.arad.kafka.events.BackgammonEvent;
import org.moshe.arad.websocket.EmailMessage;
import org.moshe.arad.websocket.UserNameMessage;
import org.springframework.stereotype.Component;

@Component
public class EventsPollFromConsumerToFrontService {

	private static final int POLL_SIZE = 100000;
	private static final int SLEEP = 100;
	private Set<BackgammonEvent> events = new HashSet<>(POLL_SIZE);
	
	private Map<String, UserNameMessage> userNamesMesaageLoockers = new HashMap<>(POLL_SIZE);
	private Map<String, EmailMessage> userEmailsMesaageLoockers = new HashMap<>(POLL_SIZE);
	
	public boolean addEventToPool(BackgammonEvent event){
		return events.add(event);
	}
	
	public boolean isEventInPoll(UUID uuid){
		Iterator<BackgammonEvent> it = events.iterator();
		
		while(it.hasNext()){
			BackgammonEvent event = it.next();
			if(event.getUuid().equals(uuid)){
				return true;
			}
		}
		
		return false;
	}
	
	public BackgammonEvent takeEventFromPoll(UUID uuid){
		if(!this.isEventInPoll(uuid)){
			try {
				Thread.sleep(SLEEP);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return this.grabEventFromPoll(uuid);
	}
	
	private BackgammonEvent grabEventFromPoll(UUID uuid){
		if(this.isEventInPoll(uuid)){
			Iterator<BackgammonEvent> it = events.iterator();
			
			while(it.hasNext()){
				BackgammonEvent event = it.next();
				if(event.getUuid().equals(uuid)){
					it.remove();
					return event;
				}
			}
			
			return null;
		}
		else return null;
	}

	public Map<String, UserNameMessage> getUserNamesMesaageLoockers() {
		return userNamesMesaageLoockers;
	}

	public void setUserNamesMesaageLoockers(Map<String, UserNameMessage> userNamesMesaageLoockers) {
		this.userNamesMesaageLoockers = userNamesMesaageLoockers;
	}

	public Map<String, EmailMessage> getUserEmailsMesaageLoockers() {
		return userEmailsMesaageLoockers;
	}

	public void setUserEmailsMesaageLoockers(Map<String, EmailMessage> userEmailsMesaageLoockers) {
		this.userEmailsMesaageLoockers = userEmailsMesaageLoockers;
	}
}
