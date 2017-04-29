package org.moshe.arad.services;

import java.util.UUID;

import javax.annotation.Resource;

import org.moshe.arad.entities.BackgammonUser;
import org.moshe.arad.kafka.EventsPool;
import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.commands.CheckUserEmailAvailabilityCommand;
import org.moshe.arad.kafka.commands.CheckUserNameAvailabilityCommand;
import org.moshe.arad.kafka.commands.CreateNewUserCommand;
import org.moshe.arad.kafka.events.NewUserCreatedAckEvent;
import org.moshe.arad.kafka.events.UserEmailAvailabilityCheckedEvent;
import org.moshe.arad.kafka.events.UserNameAvailabilityCheckedEvent;
import org.moshe.arad.kafka.producers.commands.SimpleCommandsProducer;
import org.moshe.arad.kafka.producers.config.SimpleProducerConfig;
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

	@Autowired
	private SimpleCommandsProducer<CreateNewUserCommand> simpleCreateNewUserCommandProducer;
	
	@Autowired
	private SimpleCommandsProducer<CheckUserNameAvailabilityCommand> simpleCheckUserNameAvailabilityCommandProducer;
	
	@Autowired
	private SimpleCommandsProducer<CheckUserEmailAvailabilityCommand> simpleCheckUserEmailAvailabilityCommandProducer;
	
	@Autowired
	private EventsPool eventsPoll;	
	
	@Autowired
	private IBackgammonUserRepository backgammonUserRepository;	
	
	private ApplicationContext context;
	
	private Logger logger = LoggerFactory.getLogger(HomeService.class);
	
	
	
	public boolean isUserNameAvailable(UserNameMessage userNameMessage){
		UUID uuid;
		
		simpleCheckUserNameAvailabilityCommandProducer.setTopic(KafkaUtils.CHECK_USER_NAME_AVAILABILITY_COMMAND_TOPIC);
		CheckUserNameAvailabilityCommand checkUserNameAvailabilityCommand = getUserNameCommand(userNameMessage);
		
		uuid = simpleCheckUserNameAvailabilityCommandProducer.sendKafkaMessage(checkUserNameAvailabilityCommand);
		eventsPoll.getUserNamesLockers().put(uuid.toString(), Thread.currentThread());
		
		synchronized (Thread.currentThread()) {
			
			try {
				Thread.currentThread().wait(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				return false;
			}
		}
		
		UserNameAvailabilityCheckedEvent userNameAvailabilityCheckedEvent = (UserNameAvailabilityCheckedEvent) eventsPoll.takeEventFromPoll(uuid);
		return userNameAvailabilityCheckedEvent.isAvailable();
	}	
	
	public boolean isEmailAvailable(EmailMessage emailMessage){
		UUID uuid;
		
		simpleCheckUserEmailAvailabilityCommandProducer.setTopic(KafkaUtils.CHECK_USER_EMAIL_AVAILABILITY_COMMAND_TOPIC);				
		CheckUserEmailAvailabilityCommand checkUserEmailAvailabilityCommand = getEmailCommand(emailMessage);
		
		uuid = simpleCheckUserEmailAvailabilityCommandProducer.sendKafkaMessage(checkUserEmailAvailabilityCommand);
		eventsPoll.getEmailsLockers().put(uuid.toString(), Thread.currentThread());
		
		synchronized (Thread.currentThread()) {
			try {
				Thread.currentThread().wait(5000);
			} catch (InterruptedException e) {				
				e.printStackTrace();
				return false;
			}
		}
		
		UserEmailAvailabilityCheckedEvent userEmailAvailabilityCheckedEvent = (UserEmailAvailabilityCheckedEvent) eventsPoll.takeEventFromPoll(uuid);
		return userEmailAvailabilityCheckedEvent.isAvailable();
	}

	public boolean createNewUser(BackgammonUser backgammonUser){
		UUID uuid;
		
		simpleCreateNewUserCommandProducer.setTopic(KafkaUtils.CREATE_NEW_USER_COMMAND_TOPIC);
		CreateNewUserCommand createNewUserCommand = getCreateNewUserCommand(backgammonUser);	
			
		if(!isEmailAvailable(new EmailMessage(backgammonUser.getEmail())) || !isUserNameAvailable(new UserNameMessage(backgammonUser.getUsername()))) throw new RuntimeException("Email or user name is already taken.");
		logger.info("User's email and user name are available.");
		uuid = simpleCreateNewUserCommandProducer.sendKafkaMessage(createNewUserCommand);
		eventsPoll.getCreateUserLockers().put(uuid.toString(), Thread.currentThread());
		
		synchronized (Thread.currentThread()) {
			try {
				Thread.currentThread().wait(5000);
			} catch (InterruptedException e) {				
				e.printStackTrace();
				return false;
			}
		}
		
		NewUserCreatedAckEvent newUserCreatedAckEvent = (NewUserCreatedAckEvent) eventsPoll.takeEventFromPoll(uuid);
		
		if(newUserCreatedAckEvent.isUserCreated()){
			saveUserAndAuthenticate(backgammonUser);
			return true;
		}
		else return false;
	}

	private CreateNewUserCommand getCreateNewUserCommand(BackgammonUser backgammonUser) {
		CreateNewUserCommand createNewUserCommand = context.getBean(CreateNewUserCommand.class);
		createNewUserCommand.setBackgammonUser(backgammonUser);
		return createNewUserCommand;
	}

	private void saveUserAndAuthenticate(BackgammonUser backgammonUser) {
		backgammonUser.setEnabled(true);
		backgammonUserRepository.save(backgammonUser);
		authenticateUser(backgammonUser);
		logger.info("User saved to local DB, and authenticated.");
	}
	
	private CheckUserNameAvailabilityCommand getUserNameCommand(UserNameMessage userNameMessage) {
		CheckUserNameAvailabilityCommand checkUserNameAvailabilityCommand = context.getBean(CheckUserNameAvailabilityCommand.class);			
		checkUserNameAvailabilityCommand.setUserName(userNameMessage.getUserName());
		return checkUserNameAvailabilityCommand;
	}
	
	private CheckUserEmailAvailabilityCommand getEmailCommand(EmailMessage emailMessage) {
		CheckUserEmailAvailabilityCommand checkUserEmailAvailabilityCommand = context.getBean(CheckUserEmailAvailabilityCommand.class);			
		checkUserEmailAvailabilityCommand.setEmail(emailMessage.getEmail());
		return checkUserEmailAvailabilityCommand;
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

	public EventsPool getEventsPoll() {
		return eventsPoll;
	}

	public void setEventsPoll(EventsPool eventsPoll) {
		this.eventsPoll = eventsPoll;
	}
}



















