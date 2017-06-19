package org.moshe.arad.kafka.producers.commands;

import java.util.UUID;

import org.moshe.arad.kafka.commands.ICommand;
import org.moshe.arad.kafka.producers.ISimpleProducer;

public interface ISimpleCommandProducer <T extends ICommand> extends ISimpleProducer{

	public UUID sendKafkaMessage(T command);
	
	default public UUID generateUUID(){
		return UUID.randomUUID();
	}
}
