package org.moshe.arad.kafka.commands;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class GetLobbyUpdateViewCommand extends Command {

	private String username;

	public GetLobbyUpdateViewCommand() {
	
	}
	
	public GetLobbyUpdateViewCommand(String username) {
		super();
		this.username = username;
	}

	@Override
	public String toString() {
		return "GetLobbyUpdateViewCommand [username=" + username + "]";
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
