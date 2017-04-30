package org.moshe.arad.kafka.producers;

import java.util.UUID;

import org.moshe.arad.kafka.commands.ICommand;

public interface ISimpleProducer <T extends ICommand>{

	public UUID sendKafkaMessage(T command);
}
