package org.moshe.arad.kafka.commands;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component("CheckUserEmailAvailabilityCommand")
public class CheckUserEmailCommand extends Command {

	private String email;

	public CheckUserEmailCommand() {
	}
	
	public CheckUserEmailCommand(UUID uuid, String email) {
		super(uuid);
		this.email = email;
	}

	@Override
	public String toString() {
		return "CheckUserEmailAvailabilityCommand [email=" + email + "]";
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
