package org.moshe.arad.kafka.producers;

import org.moshe.arad.kafka.commands.Commandable;

public interface SimpleProducer {

	public void sendKafkaMessage(String topicName, Commandable command);
}
