package org.moshe.arad.replies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.moshe.arad.entities.GameRoom;
import org.moshe.arad.entities.LobbyViewChanges;
import org.moshe.arad.kafka.events.GetLobbyUpdateViewAckEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class GetLobbyUpdateViewReply {
	
	private List<GameRoom> gameRoomsAdd = new ArrayList<>(10000);
	private List<String> gameRoomsDelete = new ArrayList<>(10000);
	private List<GameRoom> gameRoomsUpdate = new ArrayList<>(10000);
	private List<DeleteWatcherFrom> deleteWatchers = new ArrayList<>(10000);
	private List<AddWatcherTo> addWatchers = new ArrayList<>(10000);
	private List<AddSecondTo> addSecondPlayer = new ArrayList<>(10000);
	private List<String> leavingPlayers =  new ArrayList<>(10000);
	
	public GetLobbyUpdateViewReply() {
	
	}

	public GetLobbyUpdateViewReply(List<GameRoom> gameRoomsAdd, List<String> gameRoomsDelete,
			List<DeleteWatcherFrom> deleteWatchers, List<AddWatcherTo> addWatchers) {
		super();
		this.gameRoomsAdd = gameRoomsAdd;
		this.gameRoomsDelete = gameRoomsDelete;
		this.deleteWatchers = deleteWatchers;
		this.addWatchers = addWatchers;
	}
	
	public GetLobbyUpdateViewReply(LobbyViewChanges lobbyViewChanges){
		Set<Map.Entry<Object,Object>> entries = lobbyViewChanges.getAddWatchers().entrySet();
		Iterator<Map.Entry<Object,Object>> it = entries.iterator();
		
		this.setLeavingPlayers(lobbyViewChanges.getLeavingPlayers());
		
		while(it.hasNext()){
			Map.Entry<Object,Object> entry = it.next();
			this.addWatchers.add(new AddWatcherTo(entry.getValue().toString(), entry.getKey().toString()));
		}
		
		entries = lobbyViewChanges.getDeleteWatchers().entrySet();
		it = entries.iterator();
		
		while(it.hasNext()){
			Map.Entry<Object,Object> entry = it.next();
			this.deleteWatchers.add(new DeleteWatcherFrom(entry.getValue().toString(), entry.getKey().toString()));
		}
		
		entries = lobbyViewChanges.getAddSecondPlayer().entrySet();
		it = entries.iterator();
		
		while(it.hasNext()){
			Map.Entry<Object,Object> entry = it.next();
			this.addSecondPlayer.add(new AddSecondTo(entry.getValue().toString(), entry.getKey().toString()));
		}
		
		ListIterator<GameRoom> itList = lobbyViewChanges.getGameRoomsAdd().listIterator();
		
		while(itList.hasNext()){
			GameRoom room = itList.next();
			this.gameRoomsAdd.add(room);
		}
		
		ListIterator<String> itListDeleteRooms = lobbyViewChanges.getGameRoomsDelete().listIterator();
		
		while(itListDeleteRooms.hasNext()){
			this.gameRoomsDelete.add(itListDeleteRooms.next());
		}
		
		itList = lobbyViewChanges.getGameRoomsUpdate().listIterator();
		
		while(itList.hasNext()){
			GameRoom room = itList.next();
			this.getGameRoomsUpdate().add(room);
		}
	}

	@Override
	public String toString() {
		return "GetLobbyUpdateViewReply [gameRoomsAdd=" + gameRoomsAdd + ", gameRoomsDelete=" + gameRoomsDelete
				+ ", deleteWatchers=" + deleteWatchers + ", addWatchers=" + addWatchers + "]";
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

	public List<DeleteWatcherFrom> getDeleteWatchers() {
		return deleteWatchers;
	}

	public void setDeleteWatchers(List<DeleteWatcherFrom> deleteWatchers) {
		this.deleteWatchers = deleteWatchers;
	}

	public List<AddWatcherTo> getAddWatchers() {
		return addWatchers;
	}

	public void setAddWatchers(List<AddWatcherTo> addWatchers) {
		this.addWatchers = addWatchers;
	}

	public List<AddSecondTo> getAddSecondPlayer() {
		return addSecondPlayer;
	}

	public void setAddSecondPlayer(List<AddSecondTo> addSecondPlayer) {
		this.addSecondPlayer = addSecondPlayer;
	}

	public List<GameRoom> getGameRoomsUpdate() {
		return gameRoomsUpdate;
	}

	public void setGameRoomsUpdate(List<GameRoom> gameRoomsUpdate) {
		this.gameRoomsUpdate = gameRoomsUpdate;
	}

	public List<String> getLeavingPlayers() {
		return leavingPlayers;
	}

	public void setLeavingPlayers(List<String> leavingPlayers) {
		this.leavingPlayers = leavingPlayers;
	}
}
