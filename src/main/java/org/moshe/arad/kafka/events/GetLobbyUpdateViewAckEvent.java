package org.moshe.arad.kafka.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.moshe.arad.entities.GameRoom;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class GetLobbyUpdateViewAckEvent extends BackgammonEvent{

	private List<GameRoom> gameRoomsAdd = new ArrayList<>(10000);
	private List<String> gameRoomsDelete = new ArrayList<>(10000);
	private Map<Object,Object> addWatchers = new HashMap<>(10000);
	private Map<Object,Object> deleteWatchers = new HashMap<>(10000);
	private Map<String,List<GameRoom>> gameRoomsAddPerUser = new HashMap<>(10000); 
	
	public GetLobbyUpdateViewAckEvent() {
	
	}

	public GetLobbyUpdateViewAckEvent(List<GameRoom> gameRoomsAdd, List<String> gameRoomsDelete,
			Map<Object, Object> addWatchers, Map<Object, Object> deleteWatchers) {
		super();
		this.gameRoomsAdd = gameRoomsAdd;
		this.gameRoomsDelete = gameRoomsDelete;
		this.addWatchers = addWatchers;
		this.deleteWatchers = deleteWatchers;
	}

	public GetLobbyUpdateViewAckEvent(UUID uuid, int serviceId, int eventId, Date arrived, String clazz,
			List<GameRoom> gameRoomsAdd, List<String> gameRoomsDelete, Map<Object, Object> addWatchers,
			Map<Object, Object> deleteWatchers) {
		super(uuid, serviceId, eventId, arrived, clazz);
		this.gameRoomsAdd = gameRoomsAdd;
		this.gameRoomsDelete = gameRoomsDelete;
		this.addWatchers = addWatchers;
		this.deleteWatchers = deleteWatchers;
	}

	@Override
	public String toString() {
		return "GetLobbyUpdateViewAckEvent [gameRoomsAdd=" + gameRoomsAdd + ", gameRoomsDelete=" + gameRoomsDelete
				+ ", addWatchers=" + addWatchers + ", deleteWatchers=" + deleteWatchers + "]";
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

	public Map<String, List<GameRoom>> getGameRoomsAddPerUser() {
		return gameRoomsAddPerUser;
	}

	public void setGameRoomsAddPerUser(Map<String, List<GameRoom>> gameRoomsAddPerUser) {
		this.gameRoomsAddPerUser = gameRoomsAddPerUser;
	}
}
