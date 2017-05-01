package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.springframework.stereotype.Component;

@Component
public class LogInUserAckEventConfig extends SimpleConsumerConfig {

	public LogInUserAckEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.LOG_IN_USER_ACK_EVENT_GROUP);
	}
}
