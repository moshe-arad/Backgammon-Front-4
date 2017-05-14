package org.moshe.arad.replies;

import org.moshe.arad.entities.GameRoom;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class IsGameRoomOpen {

	private boolean isGameRoomOpen;
	private GameRoom gameRoom;
	
	public IsGameRoomOpen() {
	
	}

	public IsGameRoomOpen(boolean isGameRoomOpen) {
		super();
		this.isGameRoomOpen = isGameRoomOpen;
	}
	
	public IsGameRoomOpen(boolean isGameRoomOpen, GameRoom gameRoom) {
		super();
		this.isGameRoomOpen = isGameRoomOpen;
		this.gameRoom = gameRoom;
	}

	@Override
	public String toString() {
		return "IsGameRoomOpen [isGameRoomOpen=" + isGameRoomOpen + "]";
	}

	public boolean isGameRoomOpen() {
		return isGameRoomOpen;
	}

	public void setGameRoomOpen(boolean isGameRoomOpen) {
		this.isGameRoomOpen = isGameRoomOpen;
	}

	public GameRoom getGameRoom() {
		return gameRoom;
	}

	public void setGameRoom(GameRoom gameRoom) {
		this.gameRoom = gameRoom;
	}
}
