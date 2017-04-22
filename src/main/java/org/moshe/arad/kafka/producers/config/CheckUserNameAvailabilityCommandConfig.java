package org.moshe.arad.kafka.producers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.springframework.stereotype.Component;

@Component("CheckUserNameAvailabilityCommandConfig")
public class CheckUserNameAvailabilityCommandConfig extends SimpleProducerConfig {

	public CheckUserNameAvailabilityCommandConfig() {
		super();
		super.getProperties().put("value.serializer", KafkaUtils.CHECK_USER_NAME_AVAILABILITY_COMMAND_SERIALIZER);
	}
}
