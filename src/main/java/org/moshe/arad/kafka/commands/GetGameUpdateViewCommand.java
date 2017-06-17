package org.moshe.arad.kafka.commands;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class GetGameUpdateViewCommand extends Command {
	
	private boolean isAllLevel;
	private boolean isGroupLevel;
	private boolean isUserLevel;

	private String group;
	private String user;
	
	public GetGameUpdateViewCommand() {
	
	}

	public GetGameUpdateViewCommand(boolean isAllLevel, boolean isGroupLevel, boolean isUserLevel, String group,
			String user) {
		super();
		this.isAllLevel = isAllLevel;
		this.isGroupLevel = isGroupLevel;
		this.isUserLevel = isUserLevel;
		this.group = group;
		this.user = user;
	}

	@Override
	public String toString() {
		return "GetLobbyUpdateViewCommand [isAllLevel=" + isAllLevel + ", isGroupLevel=" + isGroupLevel
				+ ", isUserLevel=" + isUserLevel + ", group=" + group + ", user=" + user + "]";
	}

	public boolean isAllLevel() {
		return isAllLevel;
	}

	public void setAllLevel(boolean isAllLevel) {
		this.isAllLevel = isAllLevel;
	}

	public boolean isGroupLevel() {
		return isGroupLevel;
	}

	public void setGroupLevel(boolean isGroupLevel) {
		this.isGroupLevel = isGroupLevel;
	}

	public boolean isUserLevel() {
		return isUserLevel;
	}

	public void setUserLevel(boolean isUserLevel) {
		this.isUserLevel = isUserLevel;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
}
