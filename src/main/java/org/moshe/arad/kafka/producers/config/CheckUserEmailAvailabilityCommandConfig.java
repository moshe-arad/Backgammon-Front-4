package org.moshe.arad.kafka.producers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.producers.config.SimpleProducerConfig;
import org.springframework.stereotype.Component;

@Component("CheckUserEmailAvailabilityConfig")
public class CheckUserEmailAvailabilityCommandConfig extends SimpleProducerConfig {
	
	public CheckUserEmailAvailabilityCommandConfig() {
		super();
		super.getProperties().put("value.serializer", KafkaUtils.CHECK_EMAIL_AVAILABILITY_COMMAND_SERIALIZER);
	}
}
