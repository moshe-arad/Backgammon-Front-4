package org.moshe.arad.kafka.events;

import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class UserNameAvailabilityCheckedEvent extends BackgammonEvent{
	
	private boolean isAvailable;
	
	public UserNameAvailabilityCheckedEvent() {
	}
	
	public UserNameAvailabilityCheckedEvent(UUID uuid, int serviceId, String serviceName, int entityId,
			String entityType, int eventId, String eventType, boolean isAvailable) {
		super(uuid, serviceId, serviceName, entityId, entityType, eventId, eventType);
		this.isAvailable = isAvailable;
	}

	public UserNameAvailabilityCheckedEvent(UUID uuid, int serviceId, String serviceName, int entityId,
			String entityType, int eventId, String eventType, Date arrived, boolean isAvailable) {
		super(uuid, serviceId, serviceName, entityId, entityType, eventId, eventType, arrived);
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
