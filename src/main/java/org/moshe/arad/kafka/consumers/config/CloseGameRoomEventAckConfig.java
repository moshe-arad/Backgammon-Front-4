package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.springframework.stereotype.Component;

@Component
public class CloseGameRoomEventAckConfig extends SimpleConsumerConfig {

	public CloseGameRoomEventAckConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.CLOSE_GAME_ROOM_EVENT_ACK_GROUP);
	}
}
