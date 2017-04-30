package org.moshe.arad.websocket;

public class EmailMessage {

	private String email;

	public EmailMessage() {
	}
	
	public EmailMessage(String email) {
		this.email = email;
	}
	
	@Override
	public String toString() {
		return "EmailMessage [email=" + email + "]";
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
