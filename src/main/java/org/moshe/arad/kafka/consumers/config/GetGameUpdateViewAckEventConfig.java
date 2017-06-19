package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.springframework.stereotype.Component;

@Component
public class GetGameUpdateViewAckEventConfig extends SimpleConsumerConfig {

	public GetGameUpdateViewAckEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.GET_GAME_UPDATE_VIEW_ACK_EVENT_GROUP);
	}
}
