package org.moshe.arad.replies;

public class IsGameRoomOpen {

	private boolean isGameRoomOpen;
	
	public IsGameRoomOpen() {
	
	}

	public IsGameRoomOpen(boolean isGameRoomOpen) {
		super();
		this.isGameRoomOpen = isGameRoomOpen;
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
}
