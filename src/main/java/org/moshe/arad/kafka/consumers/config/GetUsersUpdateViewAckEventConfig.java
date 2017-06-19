package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.springframework.stereotype.Component;

@Component
public class GetUsersUpdateViewAckEventConfig extends SimpleConsumerConfig {

	public GetUsersUpdateViewAckEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.GET_USERS_UPDATE_VIEW_ACK_EVENT_GROUP);
	}
}
