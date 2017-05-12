package org.moshe.arad.kafka.commands;

import java.util.UUID;

import org.moshe.arad.entities.BackgammonUser;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("CreateNewUserCommand")
@Scope("prototype")
public class CreateNewUserCommand extends Command{

	private BackgammonUser backgammonUser;
	
	public CreateNewUserCommand() {
	}
	
	public CreateNewUserCommand(UUID uuid, BackgammonUser backgammonUser) {
		super(uuid);
		this.backgammonUser = backgammonUser;
	}

	@Override
	public String toString() {
		return "CreateNewUserCommand [backgammonUser=" + backgammonUser + "]";
	}

	public BackgammonUser getBackgammonUser() {
		return backgammonUser;
	}

	public void setBackgammonUser(BackgammonUser backgammonUser) {
		this.backgammonUser = backgammonUser;
	}

}
