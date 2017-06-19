package org.moshe.arad.replies;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class AddWatcherTo {

	private String watcher;
	private String gameRoomName;
	
	public AddWatcherTo() {
	
	}

	public AddWatcherTo(String watcher, String gameRoomName) {
		super();
		this.watcher = watcher;
		this.gameRoomName = gameRoomName;
	}

	@Override
	public String toString() {
		return "AddWatcherTo [watcher=" + watcher + ", gameRoomName=" + gameRoomName + "]";
	}

	public String getWatcher() {
		return watcher;
	}

	public void setWatcher(String watcher) {
		this.watcher = watcher;
	}

	public String getGameRoomName() {
		return gameRoomName;
	}

	public void setGameRoomName(String gameRoomName) {
		this.gameRoomName = gameRoomName;
	}
}
