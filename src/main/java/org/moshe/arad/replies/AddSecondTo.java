package org.moshe.arad.replies;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class AddSecondTo {

	private String secondPlayer;
	private String gameRoomName;
	
	public AddSecondTo() {
	
	}

	public AddSecondTo(String secondPlayer, String gameRoomName) {
		super();
		this.secondPlayer = secondPlayer;
		this.gameRoomName = gameRoomName;
	}

	@Override
	public String toString() {
		return "AddSecondTo [secondPlayer=" + secondPlayer + ", gameRoomName=" + gameRoomName + "]";
	}

	public String getSecondPlayer() {
		return secondPlayer;
	}

	public void setSecondPlayer(String secondPlayer) {
		this.secondPlayer = secondPlayer;
	}

	public String getGameRoomName() {
		return gameRoomName;
	}

	public void setGameRoomName(String gameRoomName) {
		this.gameRoomName = gameRoomName;
	}
}
