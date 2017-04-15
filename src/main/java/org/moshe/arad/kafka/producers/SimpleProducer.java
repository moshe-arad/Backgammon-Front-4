package org.moshe.arad.kafka.producers;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.moshe.arad.controllers.UsersController;
import org.moshe.arad.kafka.KafkaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SimpleProducer <T> {

	private final Logger logger = LoggerFactory.getLogger(UsersController.class);
	
	private Properties properties;
	
	public SimpleProducer() {
		properties = new Properties();
		properties.put("bootstrap.servers", KafkaUtils.SERVERS);
		properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		properties.put("value.serializer", "org.moshe.arad.kafka.serializers.CreateNewUserCommandSerializer");
	}
	
	public SimpleProducer(String customValueSerializer) {
		properties = new Properties();
		properties.put("bootstrap.servers", KafkaUtils.SERVERS);
		properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		properties.put("value.serializer", customValueSerializer);
	}
	
	public void sendKafkaMessage(String topicName, T value){
		logger.info("Creating kafka producer.");
		Producer<String, T> producer = new KafkaProducer<>(properties);
		logger.info("Kafka producer created.");
		
		logger.info("Sending message to topic = " + topicName + ", message = " + value.toString() + ".");
		ProducerRecord<String, T> record = new ProducerRecord<String, T>(topicName, value);
		producer.send(record);
		logger.info("Message sent.");
		producer.close();
		logger.info("Kafka producer closed.");
	}
	
}
