package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.springframework.stereotype.Component;

@Component
public class GetLobbyUpdateViewAckEventConfig extends SimpleConsumerConfig {

	public GetLobbyUpdateViewAckEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.GET_LOBBY_UPDATE_VIEW_ACK_EVENT_GROUP);
	}
}
