package org.moshe.arad.kafka.events;

import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class UserEmailAckEvent extends BackgammonEvent{
	
	private boolean isAvailable;
	
	public UserEmailAckEvent() {
	}

	public UserEmailAckEvent(UUID uuid, int serviceId, int eventId, Date arrived, String clazz,
			boolean isAvailable) {
		super(uuid, serviceId, eventId, arrived, clazz);
		this.isAvailable = isAvailable;
	}

	@Override
	public String toString() {
		return "UserEmailAvailabilityCheckedEvent [isAvailable=" + isAvailable + "]";
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
}
