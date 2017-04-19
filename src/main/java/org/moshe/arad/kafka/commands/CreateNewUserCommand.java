package org.moshe.arad.kafka.commands;

import org.moshe.arad.entities.BackgammonUser;
import org.springframework.stereotype.Component;

@Component
public class CreateNewUserCommand implements Commandable{

	private BackgammonUser backgammonUser;
	
	public CreateNewUserCommand() {
	}

	public CreateNewUserCommand(BackgammonUser backgammonUser) {
		super();
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
