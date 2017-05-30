package org.moshe.arad.kafka.events;

import org.moshe.arad.entities.UsersViewChanges;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class GetUsersUpdateViewAckEvent extends BackgammonEvent{
	
	private UsersViewChanges usersViewChanges;
	
	public GetUsersUpdateViewAckEvent() {
	
	}

	public GetUsersUpdateViewAckEvent(UsersViewChanges usersViewChanges) {
		super();
		this.usersViewChanges = usersViewChanges;
	}

	@Override
	public String toString() {
		return "GetUsersUpdateViewAckEvent [usersViewChanges=" + usersViewChanges + "]";
	}

	public UsersViewChanges getUsersViewChanges() {
		return usersViewChanges;
	}

	public void setUsersViewChanges(UsersViewChanges usersViewChanges) {
		this.usersViewChanges = usersViewChanges;
	}
}
