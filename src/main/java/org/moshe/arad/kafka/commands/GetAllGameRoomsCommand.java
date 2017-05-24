package org.moshe.arad.kafka.commands;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class GetAllGameRoomsCommand extends Command{

	private String username;

	public GetAllGameRoomsCommand(String username) {
		super();
		this.username = username;
	}
	
	@Override
	public String toString() {
		return "GetAllGameRoomsCommand [username=" + username + "]";
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
