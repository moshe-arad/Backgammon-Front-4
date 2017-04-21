package org.moshe.arad.kafka.producers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.producers.config.SimpleProducerConfig;
import org.springframework.stereotype.Component;

@Component("CheckUserEmailAvailabilityConfig")
public class CheckUserEmailAvailabilityCommandConfig extends SimpleProducerConfig {
	
	public CheckUserEmailAvailabilityCommandConfig() {
		super();
		super.getProperties().put("value.serializer", KafkaUtils.NEW_USER_CREATED_EVENT_SERIALIZER);
	}
}
