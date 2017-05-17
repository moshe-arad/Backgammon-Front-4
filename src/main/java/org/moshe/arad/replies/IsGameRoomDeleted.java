package org.moshe.arad.replies;

import org.moshe.arad.entities.GameRoom;
import org.springframework.stereotype.Component;

@Component
public class IsGameRoomDeleted {

	private boolean isGameRoomDeleted;
	private GameRoom gameRoom;
	
	public IsGameRoomDeleted() {
	
	}

	public IsGameRoomDeleted(boolean isGameRoomDeleted, GameRoom gameRoom) {
		super();
		this.isGameRoomDeleted = isGameRoomDeleted;
		this.gameRoom = gameRoom;
	}

	@Override
	public String toString() {
		return "IsGameRoomDeleted [isGameRoomDeleted=" + isGameRoomDeleted + ", gameRoom=" + gameRoom + "]";
	}

	public boolean isGameRoomDeleted() {
		return isGameRoomDeleted;
	}

	public void setGameRoomDeleted(boolean isGameRoomDeleted) {
		this.isGameRoomDeleted = isGameRoomDeleted;
	}

	public GameRoom getGameRoom() {
		return gameRoom;
	}

	public void setGameRoom(GameRoom gameRoom) {
		this.gameRoom = gameRoom;
	}
}
