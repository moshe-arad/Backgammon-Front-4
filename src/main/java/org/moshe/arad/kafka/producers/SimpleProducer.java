package org.moshe.arad.kafka.producers;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;

@Component
public class SimpleProducer <T> {

	private Properties properties;
	
	public SimpleProducer() {
		properties = new Properties();
//		:9092,192.168.1.10:9093,192.168.1.10:9094
		properties.put("bootstrap.servers", "192.168.1.10:9092");
		properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		properties.put("value.serializer", "org.moshe.arad.kafka.serializers.CreateNewUserCommandSerializer");
		properties.setProperty(ProducerConfig.RECONNECT_BACKOFF_MS_CONFIG, "10000");
	}
	
	public SimpleProducer(String customValueSerializer) {
		properties = new Properties();
//		:9092,192.168.1.10:9093,192.168.1.10:9094
		properties.put("bootstrap.servers", "192.168.1.10:9092");
		properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		properties.put("value.serializer", customValueSerializer);
		properties.setProperty(ProducerConfig.RECONNECT_BACKOFF_MS_CONFIG, "10000");
	}
	
	public void sendKafkaMessage(String topicName, T value){
		Producer<String, T> producer = new KafkaProducer<>(properties);
		
		ProducerRecord<String, T> record = new ProducerRecord<String, T>(topicName, value);
		producer.send(record);
		producer.close();
	}
	
}
