package org.moshe.arad.kafka.commands;

import java.util.UUID;

import org.moshe.arad.entities.BackgammonUserDetails;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class LogInUserCommand extends Command {

	private BackgammonUserDetails user;

	public LogInUserCommand() {
	
	}
	
	public LogInUserCommand(UUID uuid, BackgammonUserDetails user) {
		super(uuid);
		this.user = user;
	}

	public LogInUserCommand(BackgammonUserDetails user) {
		super();
		this.user = user;
	}

	@Override
	public String toString() {
		return "LogInUserCommand [user=" + user + "]";
	}

	public BackgammonUserDetails getUser() {
		return user;
	}

	public void setUser(BackgammonUserDetails user) {
		this.user = user;
	}
}
