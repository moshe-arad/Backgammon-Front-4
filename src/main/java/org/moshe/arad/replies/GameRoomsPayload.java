package org.moshe.arad.replies;

import java.util.List;

import org.moshe.arad.entities.GameRoom;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class GameRoomsPayload {

	private List<GameRoom> gameRooms;
	
	public GameRoomsPayload() {
	
	}

	public GameRoomsPayload(List<GameRoom> gameRooms) {
		super();
		this.gameRooms = gameRooms;
	}

	@Override
	public String toString() {
		return "GameRoomsPayload [gameRooms=" + gameRooms + "]";
	}

	public List<GameRoom> getGameRooms() {
		return gameRooms;
	}

	public void setGameRooms(List<GameRoom> gameRooms) {
		this.gameRooms = gameRooms;
	}
}
