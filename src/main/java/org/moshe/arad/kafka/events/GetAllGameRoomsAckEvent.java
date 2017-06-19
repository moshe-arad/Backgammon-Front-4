package org.moshe.arad.kafka.events;

import java.util.List;

import org.moshe.arad.entities.GameRoom;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class GetAllGameRoomsAckEvent extends BackgammonEvent{

	private List<GameRoom> gameRooms;
	
	public GetAllGameRoomsAckEvent() {
	
	}

	public GetAllGameRoomsAckEvent(List<GameRoom> gameRooms) {
		super();
		this.gameRooms = gameRooms;
	}

	@Override
	public String toString() {
		return "GetAllGameRoomsAckEvent [gameRooms=" + gameRooms + "]";
	}

	public List<GameRoom> getGameRooms() {
		return gameRooms;
	}

	public void setGameRooms(List<GameRoom> gameRooms) {
		this.gameRooms = gameRooms;
	}
}
