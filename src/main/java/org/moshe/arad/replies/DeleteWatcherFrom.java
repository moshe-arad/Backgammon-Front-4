package org.moshe.arad.replies;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class DeleteWatcherFrom {

	private String watcher;
	private String gameRoomName;
	
	public DeleteWatcherFrom() {
	
	}

	public DeleteWatcherFrom(String watcher, String gameRoomName) {
		super();
		this.watcher = watcher;
		this.gameRoomName = gameRoomName;
	}

	@Override
	public String toString() {
		return "DeleteWatcherFrom [watcher=" + watcher + ", gameRoomName=" + gameRoomName + "]";
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
