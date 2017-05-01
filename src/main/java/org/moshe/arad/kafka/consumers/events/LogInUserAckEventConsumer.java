package org.moshe.arad.kafka.consumers.events;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.moshe.arad.kafka.events.LogInUserAckEvent;
import org.moshe.arad.kafka.events.NewUserCreatedAckEvent;
import org.moshe.arad.services.HomeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Scope("prototype")
public class LogInUserAckEventConsumer extends SimpleEventsConsumer {

	@Autowired
	private HomeService homeService;
	
	private Logger logger = LoggerFactory.getLogger(LogInUserAckEventConsumer.class);
	
	public LogInUserAckEventConsumer() {
	
	}
	
	@Override
	public void consumerOperations(ConsumerRecord<String, String> record) {
		LogInUserAckEvent logInUserAckEvent = convertJsonBlobIntoEvent(record.value());
		Object locker = homeService.getEventsPoll().getUserLogInLockers().get(logInUserAckEvent.getUuid().toString());
		
		if(locker!= null){
			synchronized (locker) {
				logger.info("Log In User Ack Event record recieved, " + record.value());
				logger.info("passing event to home service queue...");
				homeService.getEventsPoll().addEventToPool(logInUserAckEvent);
				logger.info("New User Created Ack Event record passed to home service...");
				locker.notifyAll();
			}
		}		
	}
	
	private LogInUserAckEvent convertJsonBlobIntoEvent(String JsonBlob){
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(JsonBlob, LogInUserAckEvent.class);
		} catch (IOException e) {
			logger.error("Falied to convert Json blob into Event...");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

}
