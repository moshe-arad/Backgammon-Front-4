package org.moshe.arad.kafka.consumers.events;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.moshe.arad.kafka.consumers.ISimpleConsumer;
import org.moshe.arad.kafka.events.GetAllGameRoomsAckEvent;
import org.moshe.arad.kafka.events.UserEmailAckEvent;
import org.moshe.arad.services.HomeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Scope("prototype")
public class GetAllGameRoomsEventAckConsumer extends SimpleEventsConsumer implements ISimpleConsumer {

	@Autowired
	private HomeService homeService;
	
	private Logger logger = LoggerFactory.getLogger(GetAllGameRoomsEventAckConsumer.class);
	
	public GetAllGameRoomsEventAckConsumer() {
	}

	@Override
	public void consumerOperations(ConsumerRecord<String,String> record) {
		GetAllGameRoomsAckEvent getAllGameRoomsAckEvent = convertJsonBlobIntoEvent(record.value());
		Object locker = homeService.getEventsPoll().getGetRoomsLockers().get(getAllGameRoomsAckEvent.getUuid().toString());
		
		if(locker!= null){
			synchronized (locker) {
				logger.info("User Email Availability Checked Event record recieved, " + record.value());
				logger.info("passing event to home service queue...");
				homeService.getEventsPoll().addEventToPool(getAllGameRoomsAckEvent);
				logger.info("User Email Availability Checked Event record passed to home service...");
				locker.notifyAll();
			}
		}
						
	}
	
	private GetAllGameRoomsAckEvent convertJsonBlobIntoEvent(String JsonBlob){
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(JsonBlob, GetAllGameRoomsAckEvent.class);
		} catch (IOException e) {
			logger.error("Falied to convert Json blob into Event...");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}




	