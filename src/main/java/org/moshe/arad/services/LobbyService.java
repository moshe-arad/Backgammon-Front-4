package org.moshe.arad.services;

import java.util.UUID;

import org.moshe.arad.kafka.EventsPool;
import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.commands.AddUserAsSecondPlayerCommand;
import org.moshe.arad.kafka.commands.AddUserAsWatcherCommand;
import org.moshe.arad.kafka.commands.GetAllGameRoomsCommand;
import org.moshe.arad.kafka.commands.GetLobbyUpdateViewCommand;
import org.moshe.arad.kafka.commands.OpenNewGameRoomCommand;
import org.moshe.arad.kafka.events.GetLobbyUpdateViewAckEvent;
import org.moshe.arad.kafka.producers.commands.SimpleCommandsProducer;
import org.moshe.arad.replies.GetLobbyUpdateViewReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class LobbyService {

	@Autowired
	private SimpleCommandsProducer<OpenNewGameRoomCommand> openNewGameRoomCommandProducer;
	
	@Autowired
	private SimpleCommandsProducer<AddUserAsWatcherCommand> addUserAsWatcherCommandProducer;
	
	@Autowired
	private SimpleCommandsProducer<GetLobbyUpdateViewCommand> getLobbyUpdateViewCommandProducer;
	
	@Autowired
	private SimpleCommandsProducer<AddUserAsSecondPlayerCommand> addUserAsSecondPlayerCommandProducer;
	
	@Autowired
	private SimpleCommandsProducer<GetAllGameRoomsCommand> getAllGameRoomsCommandProducer;
	
	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private EventsPool eventsPool;
	
	private Logger logger = LoggerFactory.getLogger(LobbyService.class);
	
	public void openNewGameRoom(String username) {
		
		logger.info("Preparing an open new game room command...");
		OpenNewGameRoomCommand openNewGameRoomCommand = context.getBean(OpenNewGameRoomCommand.class);
		openNewGameRoomCommand.setUsername(username);
		
		openNewGameRoomCommandProducer.setTopic(KafkaUtils.OPEN_NEW_GAME_ROOM_COMMAND_TOPIC);
		openNewGameRoomCommandProducer.sendKafkaMessage(openNewGameRoomCommand);
	}

	public void addWatcherToGameRoom(String userNameFromJson, String gameRoomNameFromJson) {
		logger.info("Preparing an add user as watcher command...");
		
		AddUserAsWatcherCommand addUserAsWatcherCommand = context.getBean(AddUserAsWatcherCommand.class);
		addUserAsWatcherCommand.setUsername(userNameFromJson);
		addUserAsWatcherCommand.setGameRoomName(gameRoomNameFromJson);
		
		addUserAsWatcherCommandProducer.setTopic(KafkaUtils.ADD_USER_AS_WATCHER_COMMAND_TOPIC);
		addUserAsWatcherCommandProducer.sendKafkaMessage(addUserAsWatcherCommand);
	}

	public void addSecondPlayerToGameRoom(String userNameFromJson, String gameRoomNameFromJson) {
		logger.info("Preparing an add user as second player command...");
		
		AddUserAsSecondPlayerCommand addUserAsSecondPlayerCommand = context.getBean(AddUserAsSecondPlayerCommand.class);
		addUserAsSecondPlayerCommand.setUsername(userNameFromJson);
		addUserAsSecondPlayerCommand.setGameRoomName(gameRoomNameFromJson);
		
		addUserAsSecondPlayerCommandProducer.setTopic(KafkaUtils.ADD_USER_AS_SECOND_PLAYER_COMMAND_TOPIC);
		addUserAsSecondPlayerCommandProducer.sendKafkaMessage(addUserAsSecondPlayerCommand);
	}
	
	public void getAllGameRooms(String username) {
		logger.info("Preparing a get all game rooms command...");
		 		
		GetAllGameRoomsCommand getAllGameRoomsCommand = context.getBean(GetAllGameRoomsCommand.class);
		getAllGameRoomsCommand.setUsername(username);		
		getAllGameRoomsCommandProducer.setTopic(KafkaUtils.GET_ALL_GAME_ROOMS_COMMAND_TOPIC);
		getAllGameRoomsCommandProducer.sendKafkaMessage(getAllGameRoomsCommand); 		 
	}
	
	public GetLobbyUpdateViewReply getLobbyUpdateView(String all, String group, String user) {
		logger.info("Preparing a get Lobby Update view command...");
		
		GetLobbyUpdateViewCommand getLobbyUpdateViewCommand = context.getBean(GetLobbyUpdateViewCommand.class);
		
		if(all != null && !all.isEmpty() && !all.equals("none")){
			getLobbyUpdateViewCommand.setAllLevel(true);
			getLobbyUpdateViewCommand.setGroupLevel(false);
			getLobbyUpdateViewCommand.setUserLevel(false);
			getLobbyUpdateViewCommand.setGroup("");
			getLobbyUpdateViewCommand.setUser("");
		}
		
		if(group != null && !group.isEmpty() && !group.equals("none")){
			getLobbyUpdateViewCommand.setAllLevel(false);
			getLobbyUpdateViewCommand.setGroupLevel(true);
			getLobbyUpdateViewCommand.setGroup(group);
			getLobbyUpdateViewCommand.setUserLevel(false);
			getLobbyUpdateViewCommand.setUser("");
		}
		
		if(user != null && !user.isEmpty() && !user.equals("none")){
			getLobbyUpdateViewCommand.setAllLevel(false);
			getLobbyUpdateViewCommand.setGroupLevel(false);
			getLobbyUpdateViewCommand.setGroup("");
			getLobbyUpdateViewCommand.setUserLevel(true);
			getLobbyUpdateViewCommand.setUser(user);
		}
		
		getLobbyUpdateViewCommandProducer.setTopic(KafkaUtils.GET_LOBBY_UPDATE_VIEW_COMMAND_TOPIC);
		UUID uuid = getLobbyUpdateViewCommandProducer.sendKafkaMessage(getLobbyUpdateViewCommand);
		
		eventsPool.getGetUpdateViewLockers().put(uuid.toString(), Thread.currentThread());
		
		synchronized (Thread.currentThread()) {
			try {
				Thread.currentThread().wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		GetLobbyUpdateViewAckEvent getLobbyUpdateViewAckEvent = (GetLobbyUpdateViewAckEvent) eventsPool.takeEventFromPoll(uuid);
		GetLobbyUpdateViewReply getLobbyUpdateViewReply = new GetLobbyUpdateViewReply(getLobbyUpdateViewAckEvent.getLobbyViewChanges());
		return getLobbyUpdateViewReply;
	}
}
