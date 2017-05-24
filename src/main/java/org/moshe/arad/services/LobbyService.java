package org.moshe.arad.services;

import java.util.UUID;

import org.moshe.arad.kafka.EventsPool;
import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.commands.AddUserAsWatcherCommand;
import org.moshe.arad.kafka.commands.CloseGameRoomCommand;
import org.moshe.arad.kafka.commands.GetAllGameRoomsCommand;
import org.moshe.arad.kafka.commands.GetLobbyUpdateViewCommand;
import org.moshe.arad.kafka.commands.OpenNewGameRoomCommand;
import org.moshe.arad.kafka.events.CloseGameRoomEventAck;
import org.moshe.arad.kafka.events.GetAllGameRoomsAckEvent;
import org.moshe.arad.kafka.events.GetLobbyUpdateViewAckEvent;
import org.moshe.arad.kafka.events.NewGameRoomOpenedEventAck;
import org.moshe.arad.kafka.events.UserAddedAsWatcherEventAck;
import org.moshe.arad.kafka.events.UserNameAckEvent;
import org.moshe.arad.kafka.producers.commands.SimpleCommandsProducer;
import org.moshe.arad.replies.GameRoomsPayload;
import org.moshe.arad.replies.GetLobbyUpdateViewReply;
import org.moshe.arad.replies.IsGameRoomDeleted;
import org.moshe.arad.replies.IsGameRoomOpen;
import org.moshe.arad.replies.IsUserAddedAsWatcher;
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
	private SimpleCommandsProducer<CloseGameRoomCommand> closeGameRoomCommandProducer;
	
	@Autowired
	private SimpleCommandsProducer<AddUserAsWatcherCommand> addUserAsWatcherCommandProducer;
	
	@Autowired
	private SimpleCommandsProducer<GetLobbyUpdateViewCommand> getLobbyUpdateViewCommandProducer;
	
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

	@Deprecated
	public IsGameRoomDeleted closeGameRoomOpenedBy(String userNameFromJson) {
		logger.info("Preparing a close game room command...");
		
		CloseGameRoomCommand closeGameRoomCommand = context.getBean(CloseGameRoomCommand.class);
		closeGameRoomCommand.setOpenedBy(userNameFromJson);
		
		closeGameRoomCommandProducer.setTopic(KafkaUtils.CLOSE_GAME_ROOM_COMMAND_TOPIC);
		UUID uuid = closeGameRoomCommandProducer.sendKafkaMessage(closeGameRoomCommand);
		
		eventsPool.getCloseGameRoomLockers().put(uuid.toString(), Thread.currentThread());
		
		synchronized (Thread.currentThread()) {
			try {
				Thread.currentThread().wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		CloseGameRoomEventAck closeGameRoomEventAck = (CloseGameRoomEventAck) eventsPool.takeEventFromPoll(uuid);
		IsGameRoomDeleted isGameRoomDeleted = context.getBean(IsGameRoomDeleted.class);
		isGameRoomDeleted.setGameRoom(closeGameRoomEventAck.getGameRoom());
		if(closeGameRoomEventAck.isGameRoomClosed()) isGameRoomDeleted.setGameRoomDeleted(true);
		else isGameRoomDeleted.setGameRoomDeleted(false);
		
		return isGameRoomDeleted;
	}

	public IsUserAddedAsWatcher addWatcherToGameRoom(String userNameFromJson, String gameRoomNameFromJson) {
		logger.info("Preparing an add user as watcher command...");
		
		AddUserAsWatcherCommand addUserAsWatcherCommand = context.getBean(AddUserAsWatcherCommand.class);
		addUserAsWatcherCommand.setUsername(userNameFromJson);
		addUserAsWatcherCommand.setGameRoomName(gameRoomNameFromJson);
		
		addUserAsWatcherCommandProducer.setTopic(KafkaUtils.ADD_USER_AS_WATCHER_COMMAND_TOPIC);
		UUID uuid = addUserAsWatcherCommandProducer.sendKafkaMessage(addUserAsWatcherCommand);
		
		eventsPool.getUserWatcherLockers().put(uuid.toString(), Thread.currentThread());
		
		synchronized (Thread.currentThread()) {
			try {
				Thread.currentThread().wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		UserAddedAsWatcherEventAck userAddedAsWatcherEventAck = (UserAddedAsWatcherEventAck) eventsPool.takeEventFromPoll(uuid);
		IsUserAddedAsWatcher isUserAddedAsWatcher = context.getBean(IsUserAddedAsWatcher.class);
		isUserAddedAsWatcher.setGameRoom(userAddedAsWatcherEventAck.getGameRoom());
		if(userAddedAsWatcherEventAck.isUserAddedAsWatcher()) isUserAddedAsWatcher.setUserAddedAsWatcher(true);
		else isUserAddedAsWatcher.setUserAddedAsWatcher(false);
		
		return isUserAddedAsWatcher;
	}

	public GetLobbyUpdateViewReply getLobbyUpdateView(String username) {
		logger.info("Preparing a get Lobby Update view command...");
		
		GetLobbyUpdateViewCommand getLobbyUpdateViewCommand = context.getBean(GetLobbyUpdateViewCommand.class);
		getLobbyUpdateViewCommand.setUsername(username);
		
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
		GetLobbyUpdateViewReply getLobbyUpdateViewReply = new GetLobbyUpdateViewReply(getLobbyUpdateViewAckEvent);
		return getLobbyUpdateViewReply;
	}

}
