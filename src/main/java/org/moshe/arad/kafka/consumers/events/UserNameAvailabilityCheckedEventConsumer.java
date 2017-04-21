package org.moshe.arad.kafka.consumers.events;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.moshe.arad.kafka.consumers.config.SimpleConsumerConfig;
import org.moshe.arad.kafka.events.UserNameAvailabilityCheckedEvent;
import org.moshe.arad.services.HomeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class UserNameAvailabilityCheckedEventConsumer extends SimpleBackgammonEventsConsumer<UserNameAvailabilityCheckedEvent> {

	@Autowired
	private HomeService homeService;
	
	Logger logger = LoggerFactory.getLogger(UserNameAvailabilityCheckedEventConsumer.class);
	
	public UserNameAvailabilityCheckedEventConsumer(SimpleConsumerConfig simpleConsumerConfig, String topic) {
		super(simpleConsumerConfig, topic);
	}

	@Override
	public void consumerOperations(ConsumerRecord<String,UserNameAvailabilityCheckedEvent> record) {
		logger.info("User Name Availability Checked Event record recieved, " + record.value());
		logger.info("passing event to home service queue...");
		homeService.getConsumerToFrontServiceQueue().getEventsQueue().put(record.value());
		logger.info("User Name Availability Checked Event record passed to home service...");
	}	
}




	