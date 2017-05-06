package org.moshe.arad.kafka;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.moshe.arad.kafka.events.BackgammonEvent;
import org.springframework.stereotype.Component;

/**
 * 
 * @author moshe-arad
 *
 * From Consumer To Front Service
 */
@Component
public class EventsPool {

	private static final int POOL_SIZE = 100000;
	private static final int SLEEP = 100;
	private Set<BackgammonEvent> events = new HashSet<>(POOL_SIZE);
	
	private Map<String, Thread> userNamesLockers = new HashMap<>(POOL_SIZE);
	private Map<String, Thread> emailsLockers = new HashMap<>(POOL_SIZE);
	private Map<String, Thread> createUserLockers = new HashMap<>(POOL_SIZE);
	private Map<String, Thread> userLogInLockers = new HashMap<>(POOL_SIZE);
	private Map<String, Thread> userLogOutLockers = new HashMap<>(POOL_SIZE);
	
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

	public Map<String, Thread> getUserNamesLockers() {
		return userNamesLockers;
	}

	public void setUserNamesLockers(Map<String, Thread> userNamesLockers) {
		this.userNamesLockers = userNamesLockers;
	}

	public Map<String, Thread> getEmailsLockers() {
		return emailsLockers;
	}

	public void setEmailsLockers(Map<String, Thread> emailsLockers) {
		this.emailsLockers = emailsLockers;
	}

	public Map<String, Thread> getCreateUserLockers() {
		return createUserLockers;
	}

	public void setCreateUserLockers(Map<String, Thread> createUserLockers) {
		this.createUserLockers = createUserLockers;
	}

	public Map<String, Thread> getUserLogInLockers() {
		return userLogInLockers;
	}

	public void setUserLogInLockers(Map<String, Thread> userLogInLockers) {
		this.userLogInLockers = userLogInLockers;
	}

	public Map<String, Thread> getUserLogOutLockers() {
		return userLogOutLockers;
	}

	public void setUserLogOutLockers(Map<String, Thread> userLogOutLockers) {
		this.userLogOutLockers = userLogOutLockers;
	}
}
