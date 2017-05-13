package org.moshe.arad.services;

import java.util.UUID;

import org.moshe.arad.kafka.EventsPool;
import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.commands.OpenNewGameRoomCommand;
import org.moshe.arad.kafka.events.UserNameAckEvent;
import org.moshe.arad.kafka.producers.commands.SimpleCommandsProducer;
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
	private ApplicationContext context;
	
	@Autowired
	private EventsPool eventsPool;
	
	private Logger logger = LoggerFactory.getLogger(LobbyService.class);
	
	public IsGameRoomOpen openNewGameRoom(String username) {
		IsGameRoomOpen result = new IsGameRoomOpen();
		
		logger.info("Preparing an open new game room command...");
		OpenNewGameRoomCommand openNewGameRoomCommand = context.getBean(OpenNewGameRoomCommand.class);
		openNewGameRoomCommand.setUsername(username);
		
		openNewGameRoomCommandProducer.setTopic(KafkaUtils.OPEN_NEW_GAME_ROOM_COMMAND_TOPIC);
		UUID uuid = openNewGameRoomCommandProducer.sendKafkaMessage(openNewGameRoomCommand);
		
		eventsPool.getCreateUserLockers().put(uuid.toString(), Thread.currentThread());
		
		synchronized (Thread.currentThread()) {
			
			try {
				Thread.currentThread().wait();
			} catch (InterruptedException e) {
				e.printStackTrace();				
			}
		}
		
//		UserNameAckEvent userNameAvailabilityCheckedEvent = (UserNameAckEvent) eventsPool.takeEventFromPoll(uuid);
//		return userNameAvailabilityCheckedEvent.isAvailable();
		
		return null;
	}

}
