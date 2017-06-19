package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.springframework.stereotype.Component;

@Component
public class UserAddedAsWatcherEventAckConfig extends SimpleConsumerConfig {

	public UserAddedAsWatcherEventAckConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.USER_ADDED_AS_WATCHER_EVENT_ACK_GROUP);
	}
}
