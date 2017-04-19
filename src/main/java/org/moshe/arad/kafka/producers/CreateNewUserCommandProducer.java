package org.moshe.arad.kafka.producers;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.moshe.arad.controllers.UsersController;
import org.moshe.arad.kafka.commands.Commandable;
import org.moshe.arad.kafka.producers.config.SimpleProducerConfig;
import org.moshe.arad.kafka.producers.config.SimpleProducerConfigFactory;
import org.moshe.arad.kafka.producers.config.SimpleProducerConfigTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CreateNewUserCommandProducer implements SimpleProducer {

	private final Logger logger = LoggerFactory.getLogger(UsersController.class);
	
	private SimpleProducerConfig simpleProducerConfig;
	
	public CreateNewUserCommandProducer(SimpleProducerConfigTypes configType) {
		simpleProducerConfig = SimpleProducerConfigFactory.makeSimpleProducerConfig(configType);
	}
	
	public void sendKafkaMessage(String topicName, Commandable command){
		try{
			logger.info("Front Service is about to send a Command to topic=" + topicName + ", Command=" + command);
			sendMessage(topicName, command);
			logger.info("Message sent successfully, Front Service sent a Command to topic=" + topicName + ", Command=" + command);
		}
		catch(Exception ex){
			logger.error("Failed to sent message, Front Service failed to send a Command to topic=" + topicName + ", Command=" + command);
			logger.error(ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	private void sendMessage(String topicName, Commandable command){
		logger.info("Creating kafka producer.");
		Producer<String, Commandable> producer = new KafkaProducer<>(simpleProducerConfig.getProperties());
		logger.info("Kafka producer created.");
		
		logger.info("Sending message to topic = " + topicName + ", message = " + command.toString() + ".");
		ProducerRecord<String, Commandable> record = new ProducerRecord<String, Commandable>(topicName, command);
		producer.send(record);
		logger.info("Message sent.");
		producer.close();
		logger.info("Kafka producer closed.");
	}
}
