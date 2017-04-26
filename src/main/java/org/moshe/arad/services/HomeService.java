package org.moshe.arad.services;

import java.util.UUID;

import javax.annotation.Resource;

import org.moshe.arad.entities.BackgammonUser;
import org.moshe.arad.kafka.EventsPollFromConsumerToFrontService;
import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.commands.CheckUserEmailAvailabilityCommand;
import org.moshe.arad.kafka.commands.CheckUserNameAvailabilityCommand;
import org.moshe.arad.kafka.commands.CreateNewUserCommand;
import org.moshe.arad.kafka.events.UserEmailAvailabilityCheckedEvent;
import org.moshe.arad.kafka.events.UserNameAvailabilityCheckedEvent;
import org.moshe.arad.kafka.producers.SimpleCommandsProducer;
import org.moshe.arad.kafka.producers.SimpleProducerConfig;
import org.moshe.arad.repository.IBackgammonUserRepository;
import org.moshe.arad.websocket.EmailMessage;
import org.moshe.arad.websocket.UserNameMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class HomeService implements ApplicationContextAware {

	private Logger logger = LoggerFactory.getLogger(HomeService.class);

	@Autowired
	private IBackgammonUserRepository backgammonUserRepository;
	
	private ApplicationContext context;
	
	@Autowired
	private SimpleCommandsProducer<CreateNewUserCommand> simpleCreateNewUserCommandProducer;
	
	@Autowired
	private SimpleProducerConfig simpleProducerConfig;	
	
	@Autowired
	private SimpleCommandsProducer<CheckUserNameAvailabilityCommand> simpleCheckUserNameAvailabilityCommandProducer;
	
	@Autowired
	private SimpleCommandsProducer<CheckUserEmailAvailabilityCommand> simpleCheckUserEmailAvailabilityCommandProducer;
	
	@Autowired
	private EventsPollFromConsumerToFrontService eventsPollFromConsumerToFrontService;	
	
	public boolean isUserNameAvailable(UserNameMessage userNameMessage){
		UUID uuidEvent;
		simpleCheckUserNameAvailabilityCommandProducer.setSimpleProducerConfig(simpleProducerConfig);
		simpleCheckUserNameAvailabilityCommandProducer.setTopic(KafkaUtils.CHECK_USER_NAME_AVAILABILITY_COMMAND_TOPIC);
		CheckUserNameAvailabilityCommand checkUserNameAvailabilityCommand = context.getBean(CheckUserNameAvailabilityCommand.class);			
		uuidEvent = UUID.randomUUID();
		checkUserNameAvailabilityCommand.setUuid(uuidEvent);
		checkUserNameAvailabilityCommand.setUserName(userNameMessage.getUserName());
		simpleCheckUserNameAvailabilityCommandProducer.sendKafkaMessage(checkUserNameAvailabilityCommand);
		
		synchronized (userNameMessage) {
			eventsPollFromConsumerToFrontService.getUserNamesMesaageLoockers().put(uuidEvent.toString(), userNameMessage);
			try {
				userNameMessage.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		UserNameAvailabilityCheckedEvent userNameAvailabilityCheckedEvent = (UserNameAvailabilityCheckedEvent) eventsPollFromConsumerToFrontService.takeEventFromPoll(uuidEvent);
		return userNameAvailabilityCheckedEvent.isAvailable();
	}
	
	public boolean isEmailAvailable(EmailMessage emailMessage){
		UUID uuidEvent;
		simpleCheckUserEmailAvailabilityCommandProducer.setSimpleProducerConfig(simpleProducerConfig);
		simpleCheckUserEmailAvailabilityCommandProducer.setTopic(KafkaUtils.CHECK_USER_EMAIL_AVAILABILITY_COMMAND_TOPIC);		
		CheckUserEmailAvailabilityCommand checkUserEmailAvailabilityCommand = context.getBean(CheckUserEmailAvailabilityCommand.class);			
		uuidEvent = UUID.randomUUID();
		checkUserEmailAvailabilityCommand.setUuid(uuidEvent);
		checkUserEmailAvailabilityCommand.setEmail(emailMessage.getEmail());
		simpleCheckUserEmailAvailabilityCommandProducer.sendKafkaMessage(checkUserEmailAvailabilityCommand);

		synchronized (emailMessage) {
			eventsPollFromConsumerToFrontService.getUserEmailsMesaageLoockers().put(uuidEvent.toString(), emailMessage);
			try {
				emailMessage.wait();
			} catch (InterruptedException e) {				
				e.printStackTrace();
			}
		}

		
		UserEmailAvailabilityCheckedEvent userEmailAvailabilityCheckedEvent = (UserEmailAvailabilityCheckedEvent) eventsPollFromConsumerToFrontService.takeEventFromPoll(uuidEvent);
		return userEmailAvailabilityCheckedEvent.isAvailable();
	}
	
	public void createNewUser(BackgammonUser backgammonUser){
		simpleCreateNewUserCommandProducer.setSimpleProducerConfig(simpleProducerConfig);
		simpleCreateNewUserCommandProducer.setTopic(KafkaUtils.CREATE_NEW_USER_COMMAND_TOPIC);
		CreateNewUserCommand createNewUserCommand = context.getBean(CreateNewUserCommand.class);
		createNewUserCommand.setUuid(UUID.randomUUID());
		createNewUserCommand.setBackgammonUser(backgammonUser);		
		
		if(!isEmailAvailable(new EmailMessage(backgammonUser.getEmail())) || !isUserNameAvailable(new UserNameMessage(backgammonUser.getUsername()))) throw new RuntimeException("Email or user name is already taken.");
		simpleCreateNewUserCommandProducer.sendKafkaMessage(createNewUserCommand);
		logger.info("User's email and user name are available.");
		backgammonUser.setEnabled(true);
		backgammonUserRepository.save(backgammonUser);
		authenticateUser(backgammonUser);
		logger.info("User saved to local DB, and authenticated.");
	}
	
	private void authenticateUser(BackgammonUser backgammonUser) {
		Authentication auth = new UsernamePasswordAuthenticationToken(backgammonUser, 
				backgammonUser.getPassword(), backgammonUser.getAuthorities()); 
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {		
		this.context = context;
	}

	public EventsPollFromConsumerToFrontService getEventsPollFromConsumerToFrontService() {
		return eventsPollFromConsumerToFrontService;
	}

	public void setEventsPollFromConsumerToFrontService(
			EventsPollFromConsumerToFrontService eventsPollFromConsumerToFrontService) {
		this.eventsPollFromConsumerToFrontService = eventsPollFromConsumerToFrontService;
	}
}



















