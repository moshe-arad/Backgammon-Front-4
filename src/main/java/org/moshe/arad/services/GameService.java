package org.moshe.arad.services;

import java.util.UUID;

import org.moshe.arad.entities.GameViewChanges;
import org.moshe.arad.entities.LobbyViewChanges;
import org.moshe.arad.kafka.EventsPool;
import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.commands.GetGameUpdateViewCommand;
import org.moshe.arad.kafka.commands.MakeMoveCommand;
import org.moshe.arad.kafka.commands.RollDiceCommand;
import org.moshe.arad.kafka.events.GetGameUpdateViewAckEvent;
import org.moshe.arad.kafka.producers.commands.SimpleCommandsProducer;
import org.moshe.arad.replies.GetLobbyUpdateViewReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class GameService {

	@Autowired
	private SimpleCommandsProducer<GetGameUpdateViewCommand> getGameUpdateViewCommandProducer;
	
	@Autowired
	private SimpleCommandsProducer<RollDiceCommand> rollDiceCommandProducer;
	
	@Autowired
	private SimpleCommandsProducer<MakeMoveCommand> makeMoveCommandProducer;
	
	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private EventsPool eventsPool;
	
	private Logger logger = LoggerFactory.getLogger(GameService.class);
	
	
	public void rollDice(String username, String gameRoomName){
		logger.info("Preparing a roll dice command...");
		
		RollDiceCommand rollDiceCommand = context.getBean(RollDiceCommand.class);
		rollDiceCommand.setUsername(username);
		rollDiceCommand.setGameRoomName(gameRoomName);
		
		rollDiceCommandProducer.setTopic(KafkaUtils.ROLL_DICE_COMMAND_TOPIC);
		rollDiceCommandProducer.sendKafkaMessage(rollDiceCommand);
	}
	
	public void makeMove(String username, String gameRoomName, int fromColumn, int toColumn) {
		logger.info("Preparing a make move command...");
		
		MakeMoveCommand makeMoveCommand = context.getBean(MakeMoveCommand.class);
		makeMoveCommand.setUsername(username);
		makeMoveCommand.setGameRoomName(gameRoomName);
		makeMoveCommand.setFrom(fromColumn);
		makeMoveCommand.setTo(toColumn);
		
		makeMoveCommandProducer.setTopic(KafkaUtils.MAKE_MOVE_COMMAND_TOPIC);
		makeMoveCommandProducer.sendKafkaMessage(makeMoveCommand);
	}
	
	public GameViewChanges getGameUpdateView(String all, String group, String user) {
		logger.info("Preparing a get Lobby Update view command...");
		
		GetGameUpdateViewCommand getGameUpdateViewCommand = context.getBean(GetGameUpdateViewCommand.class);
		
		if(all != null && !all.isEmpty() && !all.equals("none")){
			getGameUpdateViewCommand.setAllLevel(true);
			getGameUpdateViewCommand.setGroupLevel(false);
			getGameUpdateViewCommand.setUserLevel(false);
			getGameUpdateViewCommand.setGroup("");
			getGameUpdateViewCommand.setUser("");
		}
		
		if(group != null && !group.isEmpty() && !group.equals("none")){
			getGameUpdateViewCommand.setAllLevel(false);
			getGameUpdateViewCommand.setGroupLevel(true);
			getGameUpdateViewCommand.setGroup(group);
			getGameUpdateViewCommand.setUserLevel(false);
			getGameUpdateViewCommand.setUser("");
		}
		
		if(user != null && !user.isEmpty() && !user.equals("none")){
			getGameUpdateViewCommand.setAllLevel(false);
			getGameUpdateViewCommand.setGroupLevel(false);
			getGameUpdateViewCommand.setGroup("");
			getGameUpdateViewCommand.setUserLevel(true);
			getGameUpdateViewCommand.setUser(user);
		}
		
		getGameUpdateViewCommandProducer.setTopic(KafkaUtils.GET_GAME_UPDATE_VIEW_COMMAND_TOPIC);
		UUID uuid = getGameUpdateViewCommandProducer.sendKafkaMessage(getGameUpdateViewCommand);
		
		eventsPool.getGetGameUpdateViewLockers().put(uuid.toString(), Thread.currentThread());
		
		synchronized (Thread.currentThread()) {
			try {
				Thread.currentThread().wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		GetGameUpdateViewAckEvent getGameUpdateViewAckEvent = (GetGameUpdateViewAckEvent) eventsPool.takeEventFromPoll(uuid);
		GameViewChanges gameViewChanges = getGameUpdateViewAckEvent.getGameViewChange();
		if(gameViewChanges == null) gameViewChanges = context.getBean(GameViewChanges.class);
		return gameViewChanges;
	}	
}
