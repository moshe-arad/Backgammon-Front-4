package org.moshe.arad.websocket;

public class UserNameMessage {

	private String userName;

	public UserNameMessage() {
	}
	
	public UserNameMessage(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "UserNameMessage [userName=" + userName + "]";
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
}
