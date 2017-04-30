package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.springframework.stereotype.Component;

@Component("UserNameAvailabilityCheckedEventConfig")
public class UserNameAvailabilityCheckedEventConfig extends SimpleConsumerConfig {

	public UserNameAvailabilityCheckedEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.USER_NAME_AVAILABILITY_CHECKED_EVENT_GROUP);
	}
}
