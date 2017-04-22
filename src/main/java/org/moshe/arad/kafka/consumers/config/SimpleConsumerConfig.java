package org.moshe.arad.kafka.consumers.config;

import java.util.Properties;

import org.moshe.arad.kafka.KafkaUtils;

public abstract class SimpleConsumerConfig {

	private Properties properties = new Properties();
	
	public SimpleConsumerConfig() {
		properties.put("bootstrap.servers", KafkaUtils.SERVERS);
		properties.put("key.deserializer", KafkaUtils.KEY_STRING_DESERIALIZER);
	}

	@Override
	public String toString() {
		return "SimpleProducerConfig [properties=" + properties + "]";
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
}