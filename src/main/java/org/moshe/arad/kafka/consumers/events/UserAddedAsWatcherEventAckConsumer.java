package org.moshe.arad.kafka.consumers.events;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.moshe.arad.kafka.consumers.ISimpleConsumer;
import org.moshe.arad.kafka.events.NewGameRoomOpenedEventAck;
import org.moshe.arad.kafka.events.UserAddedAsWatcherEventAck;
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
public class UserAddedAsWatcherEventAckConsumer extends SimpleEventsConsumer implements ISimpleConsumer {

	@Autowired
	private HomeService homeService;
	
	private Logger logger = LoggerFactory.getLogger(UserAddedAsWatcherEventAckConsumer.class);
	
	public UserAddedAsWatcherEventAckConsumer() {
	}

	@Override
	public void consumerOperations(ConsumerRecord<String,String> record) {
		UserAddedAsWatcherEventAck userAddedAsWatcherEventAck = convertJsonBlobIntoEvent(record.value());
		Object locker = homeService.getEventsPoll().getUserWatcherLockers().get(userAddedAsWatcherEventAck.getUuid().toString());
		
		if(locker!= null){
			synchronized (locker) {
				logger.info("User Added As Watcher Event Ack record recieved, " + record.value());
				logger.info("passing event to home service queue...");
				homeService.getEventsPoll().addEventToPool(userAddedAsWatcherEventAck);
				logger.info("User Added As Watcher Event Ack record passed to home service...");
				locker.notifyAll();
			}
		}
						
	}
	
	private UserAddedAsWatcherEventAck convertJsonBlobIntoEvent(String JsonBlob){
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(JsonBlob, UserAddedAsWatcherEventAck.class);
		} catch (IOException e) {
			logger.error("Falied to convert Json blob into Event...");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}




	