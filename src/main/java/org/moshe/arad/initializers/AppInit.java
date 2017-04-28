package org.moshe.arad.initializers;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.consumers.ISimpleConsumer;
import org.moshe.arad.kafka.consumers.config.SimpleConsumerConfig;
import org.moshe.arad.kafka.consumers.events.UserEmailAvailabilityCheckedEventConsumer;
import org.moshe.arad.kafka.consumers.events.UserNameAvailabilityCheckedEventConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class AppInit implements IAppInitializer, ApplicationContextAware	 {
	
//	@Autowired
	private UserNameAvailabilityCheckedEventConsumer userNameAvailabilityCheckedEventConsumer;
	
	@Resource(name = "UserNameAvailabilityCheckedEventConfig")
	private SimpleConsumerConfig userNameAvailabilityCheckedEventConfig;
	
//	@Autowired
	private UserEmailAvailabilityCheckedEventConsumer userEmailAvailabilityCheckedEventConsumer;
	
	@Resource(name = "UserEmailAvailabilityCheckedEventConfig")
	private SimpleConsumerConfig userEamilAvailabilityCheckedEventConfig;
	
	private ExecutorService executor = Executors.newFixedThreadPool(6);
	
	private Logger logger = LoggerFactory.getLogger(AppInit.class);
	
	private ApplicationContext context;
	
	public static final int NUM_CONSUMERS = 3;
	
	public AppInit() {
		
	}

	@Override
	public void initKafkaCommandsConsumers() {
		
	}

	@Override
	public void initKafkaEventsConsumers() {
		for(int i=0; i<NUM_CONSUMERS; i++){
			userNameAvailabilityCheckedEventConsumer = context.getBean(UserNameAvailabilityCheckedEventConsumer.class);
			userEmailAvailabilityCheckedEventConsumer = context.getBean(UserEmailAvailabilityCheckedEventConsumer.class);
			
			logger.info("Initializing user name availability checked event consumer...");		
			initSingleConsumer(userNameAvailabilityCheckedEventConsumer, KafkaUtils.USER_NAME_AVAILABILITY_CHECKED_EVENT_TOPIC, userNameAvailabilityCheckedEventConfig);		
			logger.info("Initialize user name availability checked event consumer, completed...");
			
			logger.info("Initializing user email avialability checked event consumer...");		
			initSingleConsumer(userEmailAvailabilityCheckedEventConsumer, KafkaUtils.EMAIL_AVAILABILITY_CHECKED_EVENT_TOPIC, userEamilAvailabilityCheckedEventConfig);				
			logger.info("Initialize user email avialability checked event consumer, completed...");
	
			executeProducersAndConsumers(Arrays.asList(userEmailAvailabilityCheckedEventConsumer,userNameAvailabilityCheckedEventConsumer));
		}
	}	

	@Override
	public void initKafkaCommandsProducers() {
		
	}

	@Override
	public void initKafkaEventsProducers() {
		
	}
	
	@Override
	public void engineShutdown() {
		logger.info("about to do shutdown.");
		shutdownSingleConsumer(userNameAvailabilityCheckedEventConsumer);
		shutdownSingleConsumer(userEmailAvailabilityCheckedEventConsumer);
		selfShutdown();
		logger.info("shutdown compeleted.");
		
	}
	
	private void initSingleConsumer(ISimpleConsumer consumer, String topic, SimpleConsumerConfig consumerConfig) {
		consumer.setTopic(topic);
		consumer.setSimpleConsumerConfig(consumerConfig);
		consumer.initConsumer();	
	}
	
	private void shutdownSingleConsumer(ISimpleConsumer consumer) {
		consumer.setRunning(false);
		consumer.getScheduledExecutor().shutdown();
		consumer.closeConsumer();
	}
	
	private void selfShutdown(){
		this.executor.shutdown();
	}
	
	private void executeProducersAndConsumers(List<Runnable> jobs){
		for(Runnable job:jobs)
			executor.execute(job);
	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
	}
}
