package org.moshe.arad.initializers;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.consumers.SimpleConsumer;
import org.moshe.arad.kafka.consumers.config.SimpleConsumerConfig;
import org.moshe.arad.kafka.consumers.events.SimpleBackgammonEventsConsumer;
import org.moshe.arad.kafka.events.UserEmailAvailabilityCheckedEvent;
import org.moshe.arad.kafka.events.UserNameAvailabilityCheckedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AppInit implements AppInitializer {
	
	@Resource(name = "UserNameAvailabilityCheckedEventConsumer")
	private SimpleBackgammonEventsConsumer<UserNameAvailabilityCheckedEvent> userNameAvailabilityCheckedEventConsumer;
	
	@Resource(name = "UserNameAvailabilityCheckedEventConfig")
	private SimpleConsumerConfig userNameAvailabilityCheckedEventConfig;
	
	@Resource(name = "UserEmailAvailabilityCheckedEventConsumer")
	private SimpleBackgammonEventsConsumer<UserEmailAvailabilityCheckedEvent> userEmailAvailabilityCheckedEventConsumer;
	
	@Resource(name = "UserEmailAvailabilityCheckedEventConfig")
	private SimpleConsumerConfig userEamilAvailabilityCheckedEventConfig;
	
	private ExecutorService executor = Executors.newFixedThreadPool(6);
	
	private Logger logger = LoggerFactory.getLogger(AppInit.class);
	
	public AppInit() {
		
	}

	@Override
	public void initKafkaCommandsConsumers() {
		
	}

	@Override
	public void initKafkaEventsConsumers() {
		logger.info("Initializing user name availability checked event consumer...");		
		initSingleConsumer(userNameAvailabilityCheckedEventConsumer, KafkaUtils.USER_NAME_AVAILABILITY_CHECKED_EVENT_TOPIC, userNameAvailabilityCheckedEventConfig);		
		logger.info("Initialize user name availability checked event consumer, completed...");
		
		logger.info("Initializing user email avialability checked event consumer...");		
		initSingleConsumer(userEmailAvailabilityCheckedEventConsumer, KafkaUtils.EMAIL_AVAILABILITY_CHECKED_EVENT_TOPIC, userEamilAvailabilityCheckedEventConfig);				
		logger.info("Initialize user email avialability checked event consumer, completed...");

		executeProducersAndConsumers(Arrays.asList(userNameAvailabilityCheckedEventConsumer, userEmailAvailabilityCheckedEventConsumer));
	}	

	@Override
	public void initKafkaCommandsProducers() {
		
	}

	@Override
	public void initKafkaEventsProducers() {
		
	}
	
	@Override
	public void engineShutdown() {
		logger.info("about to do shutdown.");
		shutdownSingleConsumer(userNameAvailabilityCheckedEventConsumer);
		shutdownSingleConsumer(userEmailAvailabilityCheckedEventConsumer);
		selfShutdown();
		logger.info("shutdown compeleted.");
		
	}
	
	private void initSingleConsumer(SimpleConsumer consumer, String topic, SimpleConsumerConfig consumerConfig) {
		consumer.setTopic(topic);
		consumer.setSimpleConsumerConfig(consumerConfig);
		consumer.initConsumer();	
	}
	
	private void shutdownSingleConsumer(SimpleConsumer consumer) {
		consumer.setRunning(false);
		consumer.getScheduledExecutor().shutdown();	
	}
	
	private void selfShutdown(){
		this.executor.shutdown();
	}
	
	private void executeProducersAndConsumers(List<Runnable> jobs){
		for(Runnable job:jobs)
			executor.execute(job);
	}
}
