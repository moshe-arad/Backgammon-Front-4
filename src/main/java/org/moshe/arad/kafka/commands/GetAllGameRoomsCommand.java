package org.moshe.arad.kafka.commands;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class GetAllGameRoomsCommand extends Command{

	public GetAllGameRoomsCommand() {
	
	}

	@Override
	public String toString() {
		return "GetAllGameRoomsCommand []";
	}
}
