package org.moshe.arad.kafka.consumers.events;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.moshe.arad.kafka.consumers.SimpleConsumer;
import org.moshe.arad.kafka.consumers.config.SimpleConsumerConfig;
import org.moshe.arad.kafka.events.UserEmailAvailabilityCheckedEvent;
import org.moshe.arad.services.HomeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("UserEmailAvailabilityCheckedEventConsumer")
public class UserEmailAvailabilityCheckedEventConsumer extends SimpleBackgammonEventsConsumer<UserEmailAvailabilityCheckedEvent> implements SimpleConsumer {

	@Autowired
	private HomeService homeService;
	
	Logger logger = LoggerFactory.getLogger(UserEmailAvailabilityCheckedEventConsumer.class);
	
	public UserEmailAvailabilityCheckedEventConsumer() {
	}
	
	public UserEmailAvailabilityCheckedEventConsumer(SimpleConsumerConfig simpleConsumerConfig, String topic) {
		super(simpleConsumerConfig, topic);
	}

	@Override
	public void consumerOperations(ConsumerRecord<String,UserEmailAvailabilityCheckedEvent> record) {
		Object locker = homeService.getEventsPollFromConsumerToFrontService().getUserEmailsMesaageLoockers().get(record.value().getUuid().toString());
		
		synchronized (locker) {
			logger.info("User Email Availability Checked Event record recieved, " + record.value());
			logger.info("passing event to home service queue...");
			homeService.getEventsPollFromConsumerToFrontService().addEventToPool(record.value());
			logger.info("User Email Availability Checked Event record passed to home service...");
			locker.notifyAll();
		}				
	}	
}




	