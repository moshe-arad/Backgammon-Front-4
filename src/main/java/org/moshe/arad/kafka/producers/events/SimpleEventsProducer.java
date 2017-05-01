package org.moshe.arad.kafka.producers.events;

import java.util.UUID;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.moshe.arad.kafka.commands.ICommand;
import org.moshe.arad.kafka.events.BackgammonEvent;
import org.moshe.arad.kafka.producers.config.SimpleProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * 
 * @author moshe-arad
 *
 * @param <T> is the event that we want to pass
 * 
 * important to set topic 
 * 
 * Command producer will always generate a UUID
 */
@Component
@Scope("prototype")
public class SimpleEventsProducer <T extends BackgammonEvent> implements ISimpleEventProducer<T> {

	@Autowired
	private SimpleProducerConfig simpleProducerConfig;
	
	private final Logger logger = LoggerFactory.getLogger(SimpleEventsProducer.class);
	private String topic;
	
	public SimpleEventsProducer() {
	}
	
	@Override
    public void sendKafkaMessage(T event){
		try{
			logger.info("Front Service is about to send a Command to topic=" + topic + ", Command=" + event);
			sendMessage(event);
			logger.info("Message sent successfully, Front Service sent a Command to topic=" + topic + ", Command=" + event);
		}
		catch(Exception ex){
			logger.error("Failed to sent message, Front Service failed to send a Command to topic=" + topic + ", Command=" + event);
			logger.error(ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	private void sendMessage(T event){
		logger.info("Creating kafka producer.");
		Producer<String, String> producer = new KafkaProducer<>(simpleProducerConfig.getProperties());
		logger.info("Kafka producer created.");
		
		logger.info("Generating UUID...");
		UUID uuid = generateUUID();
		logger.info("UUID was generated, uuid = " + uuid);
		event.setUuid(uuid);
		
		logger.info("Sending message to topic = " + topic + ", message = " + event.toString() + ".");
		String eventJsonBlob = convertEventIntoJsonBlob(event);
		logger.info("Sending message to topic = " + topic + ", JSON message = " + eventJsonBlob + ".");
		ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, eventJsonBlob);
		producer.send(record);
		logger.info("Message sent.");
		producer.close();
		logger.info("Kafka producer closed.");
	}

	private String convertEventIntoJsonBlob(T event){
		ObjectMapper objectMapper = new ObjectMapper();		
		try {
			return objectMapper.writeValueAsString(event);
		} catch (JsonProcessingException e) {
			logger.error("Failed to convert command into JSON blob...");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	public SimpleProducerConfig getSimpleProducerConfig() {
		return simpleProducerConfig;
	}

	@Deprecated
	public void setSimpleProducerConfig(SimpleProducerConfig simpleProducerConfig) {
		this.simpleProducerConfig = simpleProducerConfig;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}
}
