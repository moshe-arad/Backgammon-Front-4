package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.springframework.stereotype.Component;

@Component
public class LogOutUserAckEventConfig extends SimpleConsumerConfig {

	public LogOutUserAckEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.LOG_OUT_USER_ACK_EVENT_GROUP);
	}
}
