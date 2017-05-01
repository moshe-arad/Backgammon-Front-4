package org.moshe.arad.kafka.events;

import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class UserNameAckEvent extends BackgammonEvent{
	
	private boolean isAvailable;
	
	public UserNameAckEvent() {
	}

	public UserNameAckEvent(UUID uuid, int serviceId, int eventId, Date arrived, String clazz,
			boolean isAvailable) {
		super(uuid, serviceId, eventId, arrived, clazz);
		this.isAvailable = isAvailable;
	}

	@Override
	public String toString() {
		return "UserNameAvailabilityCheckedEvent [isAvailable=" + isAvailable + "]";
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
}
