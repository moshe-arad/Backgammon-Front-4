package org.moshe.arad;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.consumers.config.SimpleConsumerConfig;
import org.moshe.arad.kafka.consumers.events.SimpleBackgammonEventsConsumer;
import org.moshe.arad.kafka.events.UserNameAvailabilityCheckedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AppInit {

	private ExecutorService executor = Executors.newFixedThreadPool(6);
	
	private Logger logger = LoggerFactory.getLogger(AppInit.class);
	
	@Resource(name = "UserNameAvailabilityCheckedEventConsumer")
	private SimpleBackgammonEventsConsumer<UserNameAvailabilityCheckedEvent> userNameAvailabilityCheckedEventConsumer;
	
	@Resource(name = "UserNameAvailabilityCheckedEventConfig")
	private SimpleConsumerConfig userNameAvailabilityCheckedEventConfig;
	
	public void acceptNewEvents(){
		logger.info("Started to accept new events from services...");
		
		userNameAvailabilityCheckedEventConsumer.setTopic(KafkaUtils.USER_NAME_AVAILABILITY_CHECKED_EVENT_TOPIC);
		userNameAvailabilityCheckedEventConsumer.setSimpleConsumerConfig(userNameAvailabilityCheckedEventConfig);
		userNameAvailabilityCheckedEventConsumer.initConsumer();
		executor.execute(userNameAvailabilityCheckedEventConsumer);
	
		logger.info("Stopped to accept new events from services...");
	}
	
	public void shutdown(){
		userNameAvailabilityCheckedEventConsumer.setRunning(false);
		userNameAvailabilityCheckedEventConsumer.getScheduledExecutor().shutdown();
		
		this.executor.shutdown();
	}
}
