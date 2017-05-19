package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.springframework.stereotype.Component;

@Component
public class GetAllGameRoomsEventAckConfig extends SimpleConsumerConfig {

	public GetAllGameRoomsEventAckConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.GET_ALL_GAME_ROOMS_EVENT_ACK_GROUP);
	}
}
