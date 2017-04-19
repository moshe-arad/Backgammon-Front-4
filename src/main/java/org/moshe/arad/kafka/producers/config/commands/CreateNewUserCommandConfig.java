package org.moshe.arad.kafka.producers.config.commands;

import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.producers.config.SimpleProducerConfig;

public class CreateNewUserCommandConfig extends SimpleProducerConfig{

	public CreateNewUserCommandConfig() {
		super();
		super.getProperties().put("value.serializer", KafkaUtils.NEW_USER_CREATED_EVENT_SERIALIZER);
	}
}
