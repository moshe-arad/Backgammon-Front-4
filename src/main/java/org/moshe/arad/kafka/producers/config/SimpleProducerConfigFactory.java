package org.moshe.arad.kafka.producers.config;

import org.moshe.arad.kafka.producers.config.commands.CreateNewUserCommandConfig;

public class SimpleProducerConfigFactory {

	public static SimpleProducerConfig makeSimpleProducerConfig(SimpleProducerConfigTypes type){
		SimpleProducerConfig simpleProducerConfig = null;
		
		switch(type){
			case createNewUserCommandConfig:{
				return new CreateNewUserCommandConfig();
			}
		}
		return simpleProducerConfig;
	}
}
