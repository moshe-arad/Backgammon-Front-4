package org.moshe.arad.kafka.events;

import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class UserEmailAvailabilityCheckedEvent extends BackgammonEvent{
	
	private boolean isAvailable;
	
	public UserEmailAvailabilityCheckedEvent() {
	}

	public UserEmailAvailabilityCheckedEvent(UUID uuid, int serviceId, int eventId, Date arrived, boolean isAvailable) {
		super(uuid, serviceId, eventId, arrived);
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
