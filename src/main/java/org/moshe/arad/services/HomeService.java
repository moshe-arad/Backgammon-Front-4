package org.moshe.arad.services;

import java.util.UUID;

import javax.annotation.Resource;

import org.moshe.arad.entities.BackgammonUser;
import org.moshe.arad.kafka.EventsPollFromConsumerToFrontService;
import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.commands.CheckUserNameAvailabilityCommand;
import org.moshe.arad.kafka.commands.CreateNewUserCommand;
import org.moshe.arad.kafka.events.UserNameAvailabilityCheckedEvent;
import org.moshe.arad.kafka.producers.SimpleBackgammonCommandsProducer;
import org.moshe.arad.kafka.producers.config.SimpleProducerConfig;
import org.moshe.arad.lockers.SimpleLock;
import org.moshe.arad.repository.BackgammonUserRepository;
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
	private BackgammonUserRepository backgammonUserRepository;
	
	private ApplicationContext context;
	
	@Autowired
	private SimpleBackgammonCommandsProducer<CreateNewUserCommand> simpleCreateNewUserCommandProducer;
	
	@Resource(name = "CreateNewUserCommandConfig")
	private SimpleProducerConfig createNewUserCommandConfig;	
	
	@Autowired
	private SimpleBackgammonCommandsProducer<CheckUserNameAvailabilityCommand> simpleCheckUserNameAvailabilityCommandProducer;
	
	@Resource(name = "CheckUserNameAvailabilityCommandConfig")
	private SimpleProducerConfig CheckUserNameAvailabilityCommandConfig;
	
	@Autowired
	private EventsPollFromConsumerToFrontService eventsPollFromConsumerToFrontService;
	
	@Autowired
	private SimpleLock simpleLock;
	
	@Deprecated
	public void registerNewUser(BackgammonUser backgammonUser) {
		
//		if(!isEmailAvailable(backgammonUser.getEmail()) || !isUserNameAvailable(backgammonUser.getUsername())) throw new RuntimeException("Email or user name is already taken.");
//		logger.info("User's email and user name are available.");
//		backgammonUser.setEnabled(true);
//		backgammonUserRepository.save(backgammonUser);
//		authenticateUser(backgammonUser);
//		logger.info("User saved to local DB, and authenticated.");
	}	
	
	public boolean isUserNameAvailable(UserNameMessage userNameMessage){
		UUID uuidEvent;
		synchronized (simpleLock) {
			simpleCheckUserNameAvailabilityCommandProducer.setSimpleProducerConfig(CheckUserNameAvailabilityCommandConfig);
			simpleCheckUserNameAvailabilityCommandProducer.setTopic(KafkaUtils.CHECK_USER_NAME_AVAILABILITY_COMMAND_TOPIC);
			CheckUserNameAvailabilityCommand checkUserNameAvailabilityCommand = context.getBean(CheckUserNameAvailabilityCommand.class);			
			uuidEvent = UUID.randomUUID();
			checkUserNameAvailabilityCommand.setUuid(uuidEvent);
			checkUserNameAvailabilityCommand.setUserName(userNameMessage.getUserName());
			simpleCheckUserNameAvailabilityCommandProducer.sendKafkaMessage(checkUserNameAvailabilityCommand);
			try {
				simpleLock.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		UserNameAvailabilityCheckedEvent userNameAvailabilityCheckedEvent = (UserNameAvailabilityCheckedEvent) eventsPollFromConsumerToFrontService.takeEventFromPoll(uuidEvent);
		return userNameAvailabilityCheckedEvent.isAvailable();
	}
	
	public boolean isEmailAvailable(String email){
//		List<String> emails = backgammonUserRepository.findAll().stream().map(backgammonUser -> backgammonUser.getEmail()).collect(Collectors.toList());
//		return !emails.contains(email);
		return false;
	}
	
	public void createNewUser(BackgammonUser backgammonUser){
		simpleCreateNewUserCommandProducer.setSimpleProducerConfig(createNewUserCommandConfig);
		simpleCreateNewUserCommandProducer.setTopic(KafkaUtils.CREATE_NEW_USER_COMMAND_TOPIC);
		CreateNewUserCommand createNewUserCommand = context.getBean(CreateNewUserCommand.class);
		createNewUserCommand.setUuid(UUID.randomUUID());
		createNewUserCommand.setBackgammonUser(backgammonUser);
		simpleCreateNewUserCommandProducer.sendKafkaMessage(createNewUserCommand);		
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



















