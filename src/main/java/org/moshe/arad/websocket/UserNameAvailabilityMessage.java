package org.moshe.arad.websocket;

public class UserNameAvailabilityMessage {

	private Boolean isAvailable;

	public UserNameAvailabilityMessage() {
	}

	public UserNameAvailabilityMessage(Boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	@Override
	public String toString() {
		return "UserNameAvailabilityMessage [isAvailable=" + isAvailable + "]";
	}

	public Boolean getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(Boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
}
