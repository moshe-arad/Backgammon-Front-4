package org.moshe.arad.services;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import org.moshe.arad.entities.BackgammonUser;
import org.moshe.arad.kafka.EventsPool;
import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.commands.CheckUserEmailCommand;
import org.moshe.arad.kafka.commands.CheckUserNameCommand;
import org.moshe.arad.kafka.commands.CreateNewUserCommand;
import org.moshe.arad.kafka.commands.LogInUserCommand;
import org.moshe.arad.kafka.commands.LogOutUserCommand;
import org.moshe.arad.kafka.events.LogInUserAckEvent;
import org.moshe.arad.kafka.events.LogOutUserAckEvent;
import org.moshe.arad.kafka.events.LoggedInEvent;
import org.moshe.arad.kafka.events.LoggedOutEvent;
import org.moshe.arad.kafka.events.NewUserCreatedAckEvent;
import org.moshe.arad.kafka.events.UserEmailAckEvent;
import org.moshe.arad.kafka.events.UserNameAckEvent;
import org.moshe.arad.kafka.producers.commands.SimpleCommandsProducer;
import org.moshe.arad.kafka.producers.events.SimpleEventsProducer;
import org.moshe.arad.replies.IsUserFoundReply;
import org.moshe.arad.requests.UserCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class HomeService implements ApplicationContextAware {

	@Autowired
	private SimpleCommandsProducer<CreateNewUserCommand> simpleCreateNewUserCommandProducer;
	
	@Autowired
	private SimpleCommandsProducer<CheckUserNameCommand> simpleCheckUserNameAvailabilityCommandProducer;
	
	@Autowired
	private SimpleCommandsProducer<CheckUserEmailCommand> simpleCheckUserEmailAvailabilityCommandProducer;
	
	@Autowired
	private SimpleCommandsProducer<LogOutUserCommand> logOutUserCommandProducer;
	
	@Autowired
	private SimpleEventsProducer<LoggedOutEvent> loggedOutEventProducer;
	
	@Autowired
	private EventsPool eventsPool;	
	
	private ApplicationContext context;
	
	private Logger logger = LoggerFactory.getLogger(HomeService.class);
	
	
	public IsUserFoundReply findExistingUserAndLogin(UserCredentials userCredentials){
		IsUserFoundReply result = new IsUserFoundReply();
		
		//will update view as user loggedIn
		SimpleCommandsProducer<LogInUserCommand> logInUserCommandProducer = context.getBean(SimpleCommandsProducer.class);
		logInUserCommandProducer.setTopic(KafkaUtils.LOG_IN_USER_COMMAND_TOPIC);
		LogInUserCommand logInUserCommand = context.getBean(LogInUserCommand.class);
		
		logInUserCommand.setUser(new BackgammonUser(userCredentials.getUsername(), userCredentials.getPassword()));
		
		UUID uuid = logInUserCommandProducer.sendKafkaMessage(logInUserCommand);
		eventsPool.getUserLogInLockers().put(uuid.toString(), Thread.currentThread());
		
		synchronized (Thread.currentThread()) {
			
			try {
				Thread.currentThread().wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
				throw new RuntimeException("Failed to find user and log him in...");
			}
		}
		
		LogInUserAckEvent logInUserAckEvent = (LogInUserAckEvent) eventsPool.takeEventFromPoll(uuid);
		
		if(!logInUserAckEvent.isUserFound()){
			result.setIsUserFound(false);
			return result;
		}
		else{
			result.setIsUserFound(true);
			result.setBackgammonUser(logInUserAckEvent.getBackgammonUser());
			result.getBackgammonUser().setUser_permissions(Arrays.asList("user"));
			return result;
//			try{
//				logger.info("User was found...");
//				logger.info("Sending logged in event to mongo and lobby...");
//				SimpleEventsProducer<LoggedInEvent> loggedInEventProducer = context.getBean(SimpleEventsProducer.class);
//				loggedInEventProducer.setTopic(KafkaUtils.LOGGED_IN_EVENT_TOPIC);
//				LoggedInEvent loggedInEvent = context.getBean(LoggedInEvent.class);
//				loggedInEvent.setUuid(uuid);
//				loggedInEvent.setBackgammonUser(logInUserAckEvent.getBackgammonUser());
//				loggedInEvent.setArrived(new Date());
//				loggedInEvent.setClazz("LoggedInEvent");
//				loggedInEventProducer.sendKafkaMessage(loggedInEvent);
//				logger.info("logged in event was sent successfuly to mongo and lobby...");
//				result.setIsUserFound(true);
//				result.setBackgammonUser(logInUserAckEvent.getBackgammonUser());
//				result.getBackgammonUser().setUser_permissions(Arrays.asList("user"));
//				return result;
//			}
//			catch (Exception e) {				
//				e.printStackTrace();
//			}
		}
	}
	
	public IsUserFoundReply findExistingUserAndLogout(UserCredentials userCredentials) {
		IsUserFoundReply result = new IsUserFoundReply();
		
		BackgammonUser backgammonUser = new BackgammonUser(userCredentials.getUsername(), userCredentials.getPassword(), "", "", "", null);
		
		LogOutUserCommand logOutUserCommand = context.getBean(LogOutUserCommand.class);
		
		logOutUserCommand.setBackgammonUser(backgammonUser);
		
		logOutUserCommandProducer.setTopic(KafkaUtils.LOG_OUT_USER_COMMAND_TOPIC);
		UUID uuid = logOutUserCommandProducer.sendKafkaMessage(logOutUserCommand);
		
		eventsPool.getUserLogOutLockers().put(uuid.toString(), Thread.currentThread());
		
		synchronized (Thread.currentThread()) {
			
			try {
				Thread.currentThread().wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		LogOutUserAckEvent logOutUserAckEvent = (LogOutUserAckEvent) eventsPool.takeEventFromPoll(uuid);
		
		if(logOutUserAckEvent.isUserFound()){
			LoggedOutEvent loggedOutEvent = context.getBean(LoggedOutEvent.class);
			loggedOutEvent.setArrived(new Date());
			loggedOutEvent.setClazz("LoggedOutEvent");
			loggedOutEvent.setBackgammonUser(logOutUserAckEvent.getBackgammonUser());
			
			loggedOutEventProducer.setTopic(KafkaUtils.LOGGED_OUT_EVENT_TOPIC);
			loggedOutEventProducer.sendKafkaMessage(loggedOutEvent);
			
			result.setIsUserFound(true);
			result.setBackgammonUser(logOutUserAckEvent.getBackgammonUser());			
		}
		else result.setIsUserFound(false);
		
		return result;
	}
	
	public boolean isUserNameAvailable(String userName){
		UUID uuid;
		
		simpleCheckUserNameAvailabilityCommandProducer.setTopic(KafkaUtils.CHECK_USER_NAME_AVAILABILITY_COMMAND_TOPIC);
		CheckUserNameCommand checkUserNameAvailabilityCommand = getUserNameCommand(userName);
		
		uuid = simpleCheckUserNameAvailabilityCommandProducer.sendKafkaMessage(checkUserNameAvailabilityCommand);
		eventsPool.getUserNamesLockers().put(uuid.toString(), Thread.currentThread());
		
		synchronized (Thread.currentThread()) {
			
			try {
				Thread.currentThread().wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
				return false;
			}
		}
		
		UserNameAckEvent userNameAvailabilityCheckedEvent = (UserNameAckEvent) eventsPool.takeEventFromPoll(uuid);
		return userNameAvailabilityCheckedEvent.isAvailable();
	}	
	
	public boolean isEmailAvailable(String emailMessage){
		UUID uuid;
		
		simpleCheckUserEmailAvailabilityCommandProducer.setTopic(KafkaUtils.CHECK_USER_EMAIL_AVAILABILITY_COMMAND_TOPIC);				
		CheckUserEmailCommand checkUserEmailAvailabilityCommand = getEmailCommand(emailMessage);
		
		uuid = simpleCheckUserEmailAvailabilityCommandProducer.sendKafkaMessage(checkUserEmailAvailabilityCommand);
		eventsPool.getEmailsLockers().put(uuid.toString(), Thread.currentThread());
		
		synchronized (Thread.currentThread()) {
			try {
				Thread.currentThread().wait();
			} catch (InterruptedException e) {				
				e.printStackTrace();
				return false;
			}
		}
		
		UserEmailAckEvent userEmailAvailabilityCheckedEvent = (UserEmailAckEvent) eventsPool.takeEventFromPoll(uuid);
		return userEmailAvailabilityCheckedEvent.isAvailable();
	}

	public boolean createNewUser(BackgammonUser backgammonUser){
		UUID uuid;
		
		simpleCreateNewUserCommandProducer.setTopic(KafkaUtils.CREATE_NEW_USER_COMMAND_TOPIC);
		CreateNewUserCommand createNewUserCommand = getCreateNewUserCommand(backgammonUser);	
			
		if(!isEmailAvailable(backgammonUser.getEmail()) || !isUserNameAvailable(backgammonUser.getUserName())) throw new RuntimeException("Email or user name is already taken.");
		logger.info("User's email and user name are available.");
		uuid = simpleCreateNewUserCommandProducer.sendKafkaMessage(createNewUserCommand);
		eventsPool.getCreateUserLockers().put(uuid.toString(), Thread.currentThread());
		
		synchronized (Thread.currentThread()) {
			try {
				Thread.currentThread().wait();
			} catch (InterruptedException e) {				
				e.printStackTrace();
				return false;
			}
		}
		
		NewUserCreatedAckEvent newUserCreatedAckEvent = (NewUserCreatedAckEvent) eventsPool.takeEventFromPoll(uuid);
		
		if(newUserCreatedAckEvent.isUserCreated()) return true;
		else return false;
	}

	private CreateNewUserCommand getCreateNewUserCommand(BackgammonUser backgammonUser) {
		CreateNewUserCommand createNewUserCommand = context.getBean(CreateNewUserCommand.class);
		createNewUserCommand.setBackgammonUser(backgammonUser);
		return createNewUserCommand;
	}
	
	private CheckUserNameCommand getUserNameCommand(String userName) {
		CheckUserNameCommand checkUserNameAvailabilityCommand = context.getBean(CheckUserNameCommand.class);			
		checkUserNameAvailabilityCommand.setUserName(userName);
		return checkUserNameAvailabilityCommand;
	}
	
	private CheckUserEmailCommand getEmailCommand(String email) {
		CheckUserEmailCommand checkUserEmailAvailabilityCommand = context.getBean(CheckUserEmailCommand.class);			
		checkUserEmailAvailabilityCommand.setEmail(email);
		return checkUserEmailAvailabilityCommand;
	}	

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {		
		this.context = context;
	}

	public EventsPool getEventsPoll() {
		return eventsPool;
	}

	public void setEventsPoll(EventsPool eventsPoll) {
		this.eventsPool = eventsPoll;
	}	
}



















