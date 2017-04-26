package org.moshe.arad.kafka.consumers.events;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.moshe.arad.kafka.consumers.ISimpleConsumer;
import org.moshe.arad.kafka.consumers.config.SimpleConsumerConfig;

public interface ISimpleEventConsumer extends ISimpleConsumer {

	@Override
	public void setTopic(String topic);
	
	@Override
	public void setSimpleConsumerConfig(SimpleConsumerConfig simpleConsumerConfig);
	
	@Override
	public void initConsumer();
	
	@Override
	public void setRunning(boolean isRunning);
	
	@Override
	public ScheduledThreadPoolExecutor getScheduledExecutor();
}
