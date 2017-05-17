package org.moshe.arad.services;

import java.util.UUID;

import org.moshe.arad.kafka.EventsPool;
import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.commands.CloseGameRoomCommand;
import org.moshe.arad.kafka.commands.OpenNewGameRoomCommand;
import org.moshe.arad.kafka.events.CloseGameRoomEventAck;
import org.moshe.arad.kafka.events.NewGameRoomOpenedEventAck;
import org.moshe.arad.kafka.events.UserNameAckEvent;
import org.moshe.arad.kafka.producers.commands.SimpleCommandsProducer;
import org.moshe.arad.replies.IsGameRoomDeleted;
import org.moshe.arad.replies.IsGameRoomOpen;
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
	private ApplicationContext context;
	
	@Autowired
	private EventsPool eventsPool;
	
	private Logger logger = LoggerFactory.getLogger(LobbyService.class);
	
	public IsGameRoomOpen openNewGameRoom(String username) {
		
		logger.info("Preparing an open new game room command...");
		OpenNewGameRoomCommand openNewGameRoomCommand = context.getBean(OpenNewGameRoomCommand.class);
		openNewGameRoomCommand.setUsername(username);
		
		openNewGameRoomCommandProducer.setTopic(KafkaUtils.OPEN_NEW_GAME_ROOM_COMMAND_TOPIC);
		UUID uuid = openNewGameRoomCommandProducer.sendKafkaMessage(openNewGameRoomCommand);
		
		eventsPool.getOpenNewGameRoomLockers().put(uuid.toString(), Thread.currentThread());
		
		synchronized (Thread.currentThread()) {
			
			try {
				Thread.currentThread().wait();
			} catch (InterruptedException e) {
				e.printStackTrace();				
			}
		}
		
		NewGameRoomOpenedEventAck newGameRoomOpenedEventAck = (NewGameRoomOpenedEventAck) eventsPool.takeEventFromPoll(uuid);
		IsGameRoomOpen isGameRoomOpen = context.getBean(IsGameRoomOpen.class);
		isGameRoomOpen.setGameRoom(newGameRoomOpenedEventAck.getGameRoom());
		
		if(newGameRoomOpenedEventAck.isGameRoomOpened()) isGameRoomOpen.setGameRoomOpen(true);
		else isGameRoomOpen.setGameRoomOpen(false);
		
		return isGameRoomOpen;
	}

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

}
