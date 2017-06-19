package org.moshe.arad.replies;

import org.moshe.arad.entities.GameRoom;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class IsUserAddedAsWatcher {

	private boolean isUserAddedAsWatcher;
	private GameRoom gameRoom;
	
	public IsUserAddedAsWatcher() {
	
	}

	public IsUserAddedAsWatcher(boolean isUserAddedAsWatcher, GameRoom gameRoom) {
		super();
		this.isUserAddedAsWatcher = isUserAddedAsWatcher;
		this.gameRoom = gameRoom;
	}

	@Override
	public String toString() {
		return "IsUserAddedAsWatcher [isUserAddedAsWatcher=" + isUserAddedAsWatcher + ", gameRoom=" + gameRoom + "]";
	}

	public boolean isUserAddedAsWatcher() {
		return isUserAddedAsWatcher;
	}

	public void setUserAddedAsWatcher(boolean isUserAddedAsWatcher) {
		this.isUserAddedAsWatcher = isUserAddedAsWatcher;
	}

	public GameRoom getGameRoom() {
		return gameRoom;
	}

	public void setGameRoom(GameRoom gameRoom) {
		this.gameRoom = gameRoom;
	}
}
