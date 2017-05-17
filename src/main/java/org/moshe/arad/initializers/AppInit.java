package org.moshe.arad.initializers;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.consumers.ISimpleConsumer;
import org.moshe.arad.kafka.consumers.config.LogInUserAckEventConfig;
import org.moshe.arad.kafka.consumers.config.LogOutUserAckEventConfig;
import org.moshe.arad.kafka.consumers.config.NewGameRoomOpenedEventAckConfig;
import org.moshe.arad.kafka.consumers.config.SimpleConsumerConfig;
import org.moshe.arad.kafka.consumers.events.LogInUserAckEventConsumer;
import org.moshe.arad.kafka.consumers.events.LogOutUserAckEventConsumer;
import org.moshe.arad.kafka.consumers.events.NewGameRoomOpenedEventAckConsumer;
import org.moshe.arad.kafka.consumers.events.NewUserCreatedAckEventConsumer;
import org.moshe.arad.kafka.consumers.events.UserEmailAckEventConsumer;
import org.moshe.arad.kafka.consumers.events.UserNameAckEventConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class AppInit implements IAppInitializer, ApplicationContextAware	 {
	
	private UserNameAckEventConsumer userNameAvailabilityCheckedEventConsumer;
	
	@Resource(name = "UserNameAvailabilityCheckedEventConfig")
	private SimpleConsumerConfig userNameAvailabilityCheckedEventConfig;
	
	private UserEmailAckEventConsumer userEmailAvailabilityCheckedEventConsumer;
	
	@Resource(name = "UserEmailAvailabilityCheckedEventConfig")
	private SimpleConsumerConfig userEamilAvailabilityCheckedEventConfig;
	
	private NewUserCreatedAckEventConsumer newUserCreatedAckEventConsumer;
	
	@Resource(name = "NewUserCreatedAckEventConfig")
	private SimpleConsumerConfig newUserCreatedAckEventConfig;
	
	private LogInUserAckEventConsumer logInUserAckEventConsumer;
	
	@Autowired
	private LogInUserAckEventConfig logInUserAckEventConfig;
	
	private LogOutUserAckEventConsumer logOutUserAckEventConsumer;
	
	@Autowired
	private LogOutUserAckEventConfig logOutUserAckEventConfig;
	
	private NewGameRoomOpenedEventAckConsumer newGameRoomOpenedEventAckConsumer;
	
	@Autowired
	private NewGameRoomOpenedEventAckConfig newGameRoomOpenedEventAckConfig;
	
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
			userNameAvailabilityCheckedEventConsumer = context.getBean(UserNameAckEventConsumer.class);
			userEmailAvailabilityCheckedEventConsumer = context.getBean(UserEmailAckEventConsumer.class);
			newUserCreatedAckEventConsumer = context.getBean(NewUserCreatedAckEventConsumer.class);
			logInUserAckEventConsumer = context.getBean(LogInUserAckEventConsumer.class);
			logOutUserAckEventConsumer = context.getBean(LogOutUserAckEventConsumer.class);
			newGameRoomOpenedEventAckConsumer = context.getBean(NewGameRoomOpenedEventAckConsumer.class);
			
			logger.info("Initializing user name availability checked event consumer...");		
			initSingleConsumer(userNameAvailabilityCheckedEventConsumer, KafkaUtils.USER_NAME_AVAILABILITY_CHECKED_EVENT_TOPIC, userNameAvailabilityCheckedEventConfig);		
			logger.info("Initialize user name availability checked event consumer, completed...");
			
			logger.info("Initializing user email avialability checked event consumer...");		
			initSingleConsumer(userEmailAvailabilityCheckedEventConsumer, KafkaUtils.EMAIL_AVAILABILITY_CHECKED_EVENT_TOPIC, userEamilAvailabilityCheckedEventConfig);				
			logger.info("Initialize user email avialability checked event consumer, completed...");
	
			logger.info("Initializing new user created ack event consumer...");		
			initSingleConsumer(newUserCreatedAckEventConsumer, KafkaUtils.NEW_USER_CREATED_ACK_EVENT_TOPIC, newUserCreatedAckEventConfig);		
			logger.info("Initialize new user created ack event consumer, completed...");
			
			initSingleConsumer(logInUserAckEventConsumer, KafkaUtils.LOG_IN_USER_ACK_EVENT_TOPIC, logInUserAckEventConfig);

			initSingleConsumer(logOutUserAckEventConsumer, KafkaUtils.LOG_OUT_USER_ACK_EVENT_TOPIC, logOutUserAckEventConfig);
						
			initSingleConsumer(newGameRoomOpenedEventAckConsumer, KafkaUtils.NEW_GAME_ROOM_OPENED_EVENT_ACK_TOPIC, newGameRoomOpenedEventAckConfig);
			
			executeProducersAndConsumers(Arrays.asList(userEmailAvailabilityCheckedEventConsumer,
					userNameAvailabilityCheckedEventConsumer, 
					newUserCreatedAckEventConsumer,
					logInUserAckEventConsumer,
					logOutUserAckEventConsumer,
					newGameRoomOpenedEventAckConsumer));
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
