package org.moshe.arad.websocket;

public class EmailAvailabilityMessage {

	private Boolean isAvailable;

	public EmailAvailabilityMessage() {
	}
	
	public EmailAvailabilityMessage(Boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	@Override
	public String toString() {
		return "EmailAvailabilityMessage [isAvailable=" + isAvailable + "]";
	}

	public Boolean getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(Boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
}
