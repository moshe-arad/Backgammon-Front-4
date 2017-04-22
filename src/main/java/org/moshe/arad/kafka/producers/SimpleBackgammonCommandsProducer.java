package org.moshe.arad.kafka.producers;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.moshe.arad.kafka.commands.Commandable;
import org.moshe.arad.kafka.producers.config.SimpleProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
/**
 * 
 * @author moshe-arad
 *
 * @param <T> is the event that we want to pass
 * 
 * important to set topic and properties before usage
 */
@Component
@Scope("prototype")
public class SimpleBackgammonCommandsProducer <T extends Commandable> implements SimpleProducer<T> {

	private final Logger logger = LoggerFactory.getLogger(SimpleBackgammonCommandsProducer.class);
	private SimpleProducerConfig simpleProducerConfig;
	private String topic;
	
//	public SimpleBackgammonCommandsProducer(SimpleProducerConfig simpleProducerConfig, String topic) {
//		this.simpleProducerConfig = simpleProducerConfig;
//		this.topic = topic;
//	}
	
	public SimpleBackgammonCommandsProducer() {
	}
	
	@Override
    public void sendKafkaMessage(T command){
		try{
			logger.info("Front Service is about to send a Command to topic=" + topic + ", Command=" + command);
			sendMessage(command);
			logger.info("Message sent successfully, Front Service sent a Command to topic=" + topic + ", Command=" + command);
		}
		catch(Exception ex){
			logger.error("Failed to sent message, Front Service failed to send a Command to topic=" + topic + ", Command=" + command);
			logger.error(ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	private void sendMessage(T command){
		logger.info("Creating kafka producer.");
		Producer<String, T> producer = new KafkaProducer<>(simpleProducerConfig.getProperties());
		logger.info("Kafka producer created.");
		
		logger.info("Sending message to topic = " + topic + ", message = " + command.toString() + ".");
		ProducerRecord<String, T> record = new ProducerRecord<String, T>(topic, command);
		producer.send(record);
		logger.info("Message sent.");
		producer.close();
		logger.info("Kafka producer closed.");
	}

	public SimpleProducerConfig getSimpleProducerConfig() {
		return simpleProducerConfig;
	}

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
