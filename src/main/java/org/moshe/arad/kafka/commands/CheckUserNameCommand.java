package org.moshe.arad.kafka.commands;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component("CheckUserNameAvailabilityCommand")
public class CheckUserNameCommand extends Command {

	private String userName;
	
	public CheckUserNameCommand() {
	}
	
	public CheckUserNameCommand(UUID uuid, String userName) {
		super(uuid);
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "CheckUserNameAvailabilityCommand [userName =" + userName + "]";
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
