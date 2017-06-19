package org.moshe.arad.replies;

import org.moshe.arad.entities.BackgammonUser;

public class IsUserFoundReply {

	private Boolean isUserFound;
	private BackgammonUser backgammonUser;
	
	public IsUserFoundReply() {
	
	}

	public IsUserFoundReply(Boolean isUserFound, BackgammonUser backgammonUser) {
		super();
		this.isUserFound = isUserFound;
		this.backgammonUser = backgammonUser;
	}

	@Override
	public String toString() {
		return "IsUserFoundReply [isUserFound=" + isUserFound + "]";
	}

	public Boolean getIsUserFound() {
		return isUserFound;
	}

	public void setIsUserFound(Boolean isUserFound) {
		this.isUserFound = isUserFound;
	}

	public BackgammonUser getBackgammonUser() {
		return backgammonUser;
	}

	public void setBackgammonUser(BackgammonUser backgammonUser) {
		this.backgammonUser = backgammonUser;
	}
}
