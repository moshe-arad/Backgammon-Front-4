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
import org.moshe.arad.kafka.commands.GetLobbyUpdateViewCommand;
import org.moshe.arad.kafka.commands.GetUsersUpdateViewCommand;
import org.moshe.arad.kafka.commands.LogInUserCommand;
import org.moshe.arad.kafka.commands.LogOutUserCommand;
import org.moshe.arad.kafka.events.GetLobbyUpdateViewAckEvent;
import org.moshe.arad.kafka.events.GetUsersUpdateViewAckEvent;
import org.moshe.arad.kafka.events.LogInUserAckEvent;
import org.moshe.arad.kafka.events.LogOutUserAckEvent;
import org.moshe.arad.kafka.events.LoggedInEvent;
import org.moshe.arad.kafka.events.LoggedOutEvent;
import org.moshe.arad.kafka.events.NewUserCreatedAckEvent;
import org.moshe.arad.kafka.events.UserEmailAckEvent;
import org.moshe.arad.kafka.events.UserNameAckEvent;
import org.moshe.arad.kafka.producers.commands.SimpleCommandsProducer;
import org.moshe.arad.kafka.producers.events.SimpleEventsProducer;
import org.moshe.arad.replies.GetLobbyUpdateViewReply;
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
	private SimpleCommandsProducer<GetUsersUpdateViewCommand> getUsersUpdateViewCommandProducer;
	
	@Autowired
	private EventsPool eventsPool;	
	
	private ApplicationContext context;
	
	private Logger logger = LoggerFactory.getLogger(HomeService.class);
	
	
	public void findExistingUserAndLogin(UserCredentials userCredentials){
		IsUserFoundReply result = new IsUserFoundReply();
		
		//will update view as user loggedIn
		SimpleCommandsProducer<LogInUserCommand> logInUserCommandProducer = context.getBean(SimpleCommandsProducer.class);
		logInUserCommandProducer.setTopic(KafkaUtils.LOG_IN_USER_COMMAND_TOPIC);
		LogInUserCommand logInUserCommand = context.getBean(LogInUserCommand.class);
		
		logInUserCommand.setUser(new BackgammonUser(userCredentials.getUsername(), userCredentials.getPassword()));
		
		logInUserCommandProducer.sendKafkaMessage(logInUserCommand);
//		eventsPool.getUserLogInLockers().put(uuid.toString(), Thread.currentThread());
//		
//		synchronized (Thread.currentThread()) {
//			
//			try {
//				Thread.currentThread().wait();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//				throw new RuntimeException("Failed to find user and log him in...");
//			}
//		}
//		
//		LogInUserAckEvent logInUserAckEvent = (LogInUserAckEvent) eventsPool.takeEventFromPoll(uuid);
//		
//		if(!logInUserAckEvent.isUserFound()){
//			result.setIsUserFound(false);
//			return result;
//		}
//		else{
//			result.setIsUserFound(true);
//			result.setBackgammonUser(logInUserAckEvent.getBackgammonUser());
//			result.getBackgammonUser().setUser_permissions(Arrays.asList("user"));
//			return result;
//		}
	}
	
	public void findExistingUserAndLogout(UserCredentials userCredentials) {
		BackgammonUser backgammonUser = new BackgammonUser(userCredentials.getUsername(), userCredentials.getPassword(), "", "", "", null);		
		LogOutUserCommand logOutUserCommand = context.getBean(LogOutUserCommand.class);		
		logOutUserCommand.setBackgammonUser(backgammonUser);
		
		logOutUserCommandProducer.setTopic(KafkaUtils.LOG_OUT_USER_COMMAND_TOPIC);
		logOutUserCommandProducer.sendKafkaMessage(logOutUserCommand);
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

	public void createNewUser(BackgammonUser backgammonUser){
		simpleCreateNewUserCommandProducer.setTopic(KafkaUtils.CREATE_NEW_USER_COMMAND_TOPIC);
		CreateNewUserCommand createNewUserCommand = getCreateNewUserCommand(backgammonUser);	
			
		if(!isEmailAvailable(backgammonUser.getEmail()) || !isUserNameAvailable(backgammonUser.getUserName())) throw new RuntimeException("Email or user name is already taken.");
		logger.info("User's email and user name are available.");
		simpleCreateNewUserCommandProducer.sendKafkaMessage(createNewUserCommand);
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

	public GetUsersUpdateViewAckEvent getUsersUpdateView(String all, String group, String user) {
		logger.info("Preparing a get Users Update view command...");
		
		GetUsersUpdateViewCommand getUsersUpdateViewCommand = context.getBean(GetUsersUpdateViewCommand.class);
		
		if(all != null && !all.isEmpty() && !all.equals("none")){
			getUsersUpdateViewCommand.setAllLevel(true);
			getUsersUpdateViewCommand.setGroupLevel(false);
			getUsersUpdateViewCommand.setUserLevel(false);
			getUsersUpdateViewCommand.setGroup("");
			getUsersUpdateViewCommand.setUser("");
		}
		
		if(group != null && !group.isEmpty() && !group.equals("none")){
			getUsersUpdateViewCommand.setAllLevel(false);
			getUsersUpdateViewCommand.setGroupLevel(true);
			getUsersUpdateViewCommand.setGroup(group);
			getUsersUpdateViewCommand.setUserLevel(false);
			getUsersUpdateViewCommand.setUser("");
		}
		
		if(user != null && !user.isEmpty() && !user.equals("none")){
			getUsersUpdateViewCommand.setAllLevel(false);
			getUsersUpdateViewCommand.setGroupLevel(false);
			getUsersUpdateViewCommand.setGroup("");
			getUsersUpdateViewCommand.setUserLevel(true);
			getUsersUpdateViewCommand.setUser(user);
		}
		
		getUsersUpdateViewCommandProducer.setTopic(KafkaUtils.GET_USERS_UPDATE_VIEW_COMMAND_TOPIC);
		UUID uuid = getUsersUpdateViewCommandProducer.sendKafkaMessage(getUsersUpdateViewCommand);
		
		eventsPool.getGetUsersUpdateViewLockers().put(uuid.toString(), Thread.currentThread());
		
		synchronized (Thread.currentThread()) {
			try {
				Thread.currentThread().wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		GetUsersUpdateViewAckEvent getUsersUpdateViewAckEvent = (GetUsersUpdateViewAckEvent) eventsPool.takeEventFromPoll(uuid);
		return getUsersUpdateViewAckEvent;
	}	
}



















