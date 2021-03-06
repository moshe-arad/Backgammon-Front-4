package org.moshe.arad.kafka.consumers.events;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.moshe.arad.kafka.consumers.ISimpleConsumer;
import org.moshe.arad.kafka.events.UserNameAckEvent;
import org.moshe.arad.services.HomeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component("UserNameAvailabilityCheckedEventConsumer")
@Scope("prototype")
public class UserNameAckEventConsumer extends SimpleEventsConsumer implements ISimpleConsumer {

	@Autowired
	private HomeService homeService;
	
	Logger logger = LoggerFactory.getLogger(UserNameAckEventConsumer.class);
	
	public UserNameAckEventConsumer() {
	}

	@Override
	public void consumerOperations(ConsumerRecord<String,String> record) {
		UserNameAckEvent userNameAvailabilityCheckedEvent = convertJsonBlobIntoEvent(record.value());
		
		Object locker = homeService.getEventsPoll().getUserNamesLockers().get(userNameAvailabilityCheckedEvent.getUuid().toString());
		synchronized (locker) {
			logger.info("User Name Availability Checked Event record recieved, " + record.value());
			logger.info("passing event to home service queue...");
			homeService.getEventsPoll().addEventToPool(userNameAvailabilityCheckedEvent);
			logger.info("User Name Availability Checked Event record passed to home service...");
			locker.notifyAll();
		}			
	}
	
	private UserNameAckEvent convertJsonBlobIntoEvent(String JsonBlob){
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(JsonBlob, UserNameAckEvent.class);
		} catch (IOException e) {
			logger.error("Falied to convert Json blob into Event...");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}




	