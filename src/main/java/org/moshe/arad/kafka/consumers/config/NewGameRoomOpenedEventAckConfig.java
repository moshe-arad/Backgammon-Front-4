package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.springframework.stereotype.Component;

@Component
public class NewGameRoomOpenedEventAckConfig extends SimpleConsumerConfig {

	public NewGameRoomOpenedEventAckConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.NEW_GAME_ROOM_OPENED_EVENT_ACK_GROUP);
	}
}
