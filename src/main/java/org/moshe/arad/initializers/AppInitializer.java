package org.moshe.arad.initializers;

import org.moshe.arad.kafka.KafkaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface AppInitializer {
	
	public static Logger logger = LoggerFactory.getLogger(AppInitializer.class);
	
	public default void startEngine(){
		logger.info("Front Service, Engine is about to start...");
		initKafkaCommandsConsumers();
		initKafkaEventsConsumers();
		initKafkaCommandsProducers();
		initKafkaEventsProducers();
		logger.info("Front Service, Engine started successfuly...");	
	}
	
	public void engineShutdown();
	
	public void initKafkaCommandsConsumers();

	public void initKafkaEventsConsumers();

	public void initKafkaCommandsProducers();

	public void initKafkaEventsProducers();
}
