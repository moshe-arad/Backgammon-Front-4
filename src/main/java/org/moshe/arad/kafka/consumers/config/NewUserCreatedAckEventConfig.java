package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.springframework.stereotype.Component;

@Component("NewUserCreatedAckEventConfig")
public class NewUserCreatedAckEventConfig extends SimpleConsumerConfig {

	public NewUserCreatedAckEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.NEW_USER_CREATED_ACK_EVENT_GROUP);
	}
}
