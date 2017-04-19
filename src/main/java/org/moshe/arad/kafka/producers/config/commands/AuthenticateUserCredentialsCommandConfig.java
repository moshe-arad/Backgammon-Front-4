package org.moshe.arad.kafka.producers.config.commands;

import org.moshe.arad.kafka.producers.config.SimpleProducerConfig;

public class AuthenticateUserCredentialsCommandConfig extends SimpleProducerConfig{

	public AuthenticateUserCredentialsCommandConfig() {
		super();
//		super.getProperties().put("value.serializer", KafkaUtils.NEW_USER_CREATED_EVENT_SERIALIZER);
	}
}
