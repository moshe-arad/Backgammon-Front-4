package org.moshe.arad.kafka.producers;

import org.moshe.arad.kafka.commands.Commandable;

public interface SimpleProducer <T extends Commandable>{

	public void sendKafkaMessage(T command);
}
