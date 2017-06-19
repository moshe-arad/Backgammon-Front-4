package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.springframework.stereotype.Component;

@Component("UserEmailAvailabilityCheckedEventConfig")
public class UserEmailAckEventConfig extends SimpleConsumerConfig {

	public UserEmailAckEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.USER_EMAIL_AVAILABILITY_CHECKED_EVENT_GROUP);
	}
}
