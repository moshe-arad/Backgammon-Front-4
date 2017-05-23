package org.moshe.arad.kafka.commands;

import java.util.UUID;

import org.moshe.arad.entities.BackgammonUser;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class GetLobbyUpdateViewCommand extends Command {

	public GetLobbyUpdateViewCommand() {
	
	}

	@Override
	public String toString() {
		return "GetLobbyUpdateViewCommand []";
	}
}
