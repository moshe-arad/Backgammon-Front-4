package org.moshe.arad.kafka.producers.commands;

import org.moshe.arad.kafka.commands.ICommand;
import org.moshe.arad.kafka.producers.ISimpleProducer;

public interface ISimpleCommandProducer <T extends ICommand> extends ISimpleProducer<T>{

}
