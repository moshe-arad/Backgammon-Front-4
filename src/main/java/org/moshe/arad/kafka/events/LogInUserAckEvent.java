package org.moshe.arad.kafka.events;

import java.util.Date;
import java.util.UUID;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class LogInUserAckEvent extends BackgammonEvent {

	private boolean isUserFound;

	public LogInUserAckEvent() {
	
	}
	
	public LogInUserAckEvent(boolean isUserFound) {
		super();
		this.isUserFound = isUserFound;
	}

	public LogInUserAckEvent(UUID uuid, int serviceId, int eventId, Date arrived, String clazz, boolean isUserFound) {
		super(uuid, serviceId, eventId, arrived, clazz);
		this.isUserFound = isUserFound;
	}

	@Override
	public String toString() {
		return "LogInUserAckEvent [isUserFound=" + isUserFound + "]";
	}

	public boolean isUserFound() {
		return isUserFound;
	}

	public void setUserFound(boolean isUserFound) {
		this.isUserFound = isUserFound;
	}
}
