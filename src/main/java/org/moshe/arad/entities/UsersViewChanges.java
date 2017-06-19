package org.moshe.arad.entities;

import java.util.ArrayList;
import java.util.List;

import org.moshe.arad.entities.BackgammonUser;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class UsersViewChanges {

	private List<BackgammonUser> newUsersAdded = new ArrayList<>(10000);
	private List<BackgammonUser> usersLoggedIn = new ArrayList<>(10000);
	private List<BackgammonUser> usersPermissionsUpdated = new ArrayList<>(10000);

	public UsersViewChanges() {
	
	}

	public UsersViewChanges(List<BackgammonUser> newUsersAdded, List<BackgammonUser> usersLoggedIn,
			List<BackgammonUser> usersStatusUpdated, List<BackgammonUser> usersPermissionsUpdated) {
		super();
		this.newUsersAdded = newUsersAdded;
		this.usersLoggedIn = usersLoggedIn;
		this.usersPermissionsUpdated = usersPermissionsUpdated;
	}

	@Override
	public String toString() {
		return "UsersViewChanges [newUsersAdded=" + newUsersAdded + ", usersLoggedIn=" + usersLoggedIn
				+ ", usersPermissionsUpdated=" + usersPermissionsUpdated + "]";
	}

	public List<BackgammonUser> getNewUsersAdded() {
		return newUsersAdded;
	}

	public void setNewUsersAdded(List<BackgammonUser> newUsersAdded) {
		this.newUsersAdded = newUsersAdded;
	}

	public List<BackgammonUser> getUsersLoggedIn() {
		return usersLoggedIn;
	}

	public void setUsersLoggedIn(List<BackgammonUser> usersLoggedIn) {
		this.usersLoggedIn = usersLoggedIn;
	}

	public List<BackgammonUser> getUsersPermissionsUpdated() {
		return usersPermissionsUpdated;
	}

	public void setUsersPermissionsUpdated(List<BackgammonUser> usersPermissionsUpdated) {
		this.usersPermissionsUpdated = usersPermissionsUpdated;
	}
}
