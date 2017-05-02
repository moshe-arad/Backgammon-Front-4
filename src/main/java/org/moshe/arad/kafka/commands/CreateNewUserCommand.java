package org.moshe.arad.kafka.commands;

import java.util.UUID;

import org.moshe.arad.entities.BackgammonUserDetails;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("CreateNewUserCommand")
@Scope("prototype")
public class CreateNewUserCommand extends Command{

	private BackgammonUserDetails backgammonUser;
	
	public CreateNewUserCommand() {
	}
	
	public CreateNewUserCommand(UUID uuid, BackgammonUserDetails backgammonUser) {
		super(uuid);
		this.backgammonUser = backgammonUser;
	}

	@Override
	public String toString() {
		return "CreateNewUserCommand [backgammonUser=" + backgammonUser + "]";
	}

	public BackgammonUserDetails getBackgammonUser() {
		return backgammonUser;
	}

	public void setBackgammonUser(BackgammonUserDetails backgammonUser) {
		this.backgammonUser = backgammonUser;
	}

}
