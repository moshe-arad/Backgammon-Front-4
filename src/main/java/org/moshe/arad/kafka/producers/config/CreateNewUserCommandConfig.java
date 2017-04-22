package org.moshe.arad.kafka.producers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.producers.config.SimpleProducerConfig;
import org.springframework.stereotype.Component;

@Component("CreateNewUserCommandConfig")
public class CreateNewUserCommandConfig extends SimpleProducerConfig{

	public CreateNewUserCommandConfig() {
		super();
		super.getProperties().put("value.serializer", KafkaUtils.CREATE_NEW_USER_COMMAND_SERIALIZER);
	}
}
