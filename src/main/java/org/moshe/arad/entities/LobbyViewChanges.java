package org.moshe.arad.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.moshe.arad.entities.GameRoom;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class LobbyViewChanges {

	private List<GameRoom> gameRoomsAdd = new ArrayList<>(10000);
	private List<String> gameRoomsDelete = new ArrayList<>(10000);
	private List<GameRoom> gameRoomsUpdate = new ArrayList<>(10000);
	private Map<Object,Object> addWatchers = new HashMap<>(10000);
	private Map<Object,Object> deleteWatchers = new HashMap<>(10000);
	private Map<Object,Object> addSecondPlayer = new HashMap<>(10000);
	
	public LobbyViewChanges() {
	
	}
	
	public LobbyViewChanges(List<GameRoom> gameRoomsAdd, List<String> gameRoomsDelete, Map<Object, Object> addWatchers,
			Map<Object, Object> deleteWatchers, Map<Object, Object> addSecondPlayer) {
		super();
		this.gameRoomsAdd = gameRoomsAdd;
		this.gameRoomsDelete = gameRoomsDelete;
		this.addWatchers = addWatchers;
		this.deleteWatchers = deleteWatchers;
		this.addSecondPlayer = addSecondPlayer;
	}

	@Override
	public String toString() {
		return "LobbyViewChanges [gameRoomsAdd=" + gameRoomsAdd + ", gameRoomsDelete=" + gameRoomsDelete
				+ ", addWatchers=" + addWatchers + ", deleteWatchers=" + deleteWatchers + ", addSecondPlayer="
				+ addSecondPlayer + "]";
	}

	public List<GameRoom> getGameRoomsAdd() {
		return gameRoomsAdd;
	}

	public void setGameRoomsAdd(List<GameRoom> gameRoomsAdd) {
		this.gameRoomsAdd = gameRoomsAdd;
	}

	public List<String> getGameRoomsDelete() {
		return gameRoomsDelete;
	}

	public void setGameRoomsDelete(List<String> gameRoomsDelete) {
		this.gameRoomsDelete = gameRoomsDelete;
	}

	public Map<Object, Object> getAddWatchers() {
		return addWatchers;
	}

	public void setAddWatchers(Map<Object, Object> addWatchers) {
		this.addWatchers = addWatchers;
	}

	public Map<Object, Object> getDeleteWatchers() {
		return deleteWatchers;
	}

	public void setDeleteWatchers(Map<Object, Object> deleteWatchers) {
		this.deleteWatchers = deleteWatchers;
	}

	public Map<Object, Object> getAddSecondPlayer() {
		return addSecondPlayer;
	}

	public void setAddSecondPlayer(Map<Object, Object> addSecondPlayer) {
		this.addSecondPlayer = addSecondPlayer;
	}

	public List<GameRoom> getGameRoomsUpdate() {
		return gameRoomsUpdate;
	}

	public void setGameRoomsUpdate(List<GameRoom> gameRoomsUpdate) {
		this.gameRoomsUpdate = gameRoomsUpdate;
	}
}
