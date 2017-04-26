package org.moshe.arad.kafka.producers;

import org.moshe.arad.kafka.commands.ICommand;

public interface ISimpleProducer <T extends ICommand>{

	public void sendKafkaMessage(T command);
}
