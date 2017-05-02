package org.moshe.arad.security;

import java.util.Date;
import java.util.UUID;

import org.moshe.arad.entities.BackgammonUserDetails;
import org.moshe.arad.kafka.EventsPool;
import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.commands.LogInUserCommand;
import org.moshe.arad.kafka.events.LogInUserAckEvent;
import org.moshe.arad.kafka.events.LoggedInEvent;
import org.moshe.arad.kafka.producers.commands.SimpleCommandsProducer;
import org.moshe.arad.kafka.producers.events.SimpleEventsProducer;
import org.moshe.arad.repository.IBackgammonUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class BackgammonUserDetailsService implements UserDetailsService{

	@Autowired
	private IBackgammonUserRepository backgammonUserRepository;
	
	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private EventsPool eventsPoll;
	
	private Logger logger = LoggerFactory.getLogger(BackgammonUserDetailsService.class);
	
	@SuppressWarnings("finally")
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		UserDetails result = backgammonUserRepository.findByUserName(userName);
		
		//will update view as user loggedIn
		SimpleCommandsProducer<LogInUserCommand> logInUserCommandProducer = context.getBean(SimpleCommandsProducer.class);
		logInUserCommandProducer.setTopic(KafkaUtils.LOG_IN_USER_COMMAND_TOPIC);
		LogInUserCommand logInUserCommand = context.getBean(LogInUserCommand.class);
		logInUserCommand.setUser((BackgammonUserDetails)result);
		
		UUID uuid = logInUserCommandProducer.sendKafkaMessage(logInUserCommand);
		eventsPoll.getUserLogInLockers().put(uuid.toString(), Thread.currentThread());
		
		synchronized (Thread.currentThread()) {
			
			try {
				Thread.currentThread().wait(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				throw new UsernameNotFoundException("Failed to find user and log him in...");
			}
		}
		
		LogInUserAckEvent logInUserAckEvent = (LogInUserAckEvent) eventsPoll.takeEventFromPoll(uuid);
		
		if(!logInUserAckEvent.isUserFound()) throw new UsernameNotFoundException("Failed to find user and log him in...");
		else{
			try{
				logger.info("User was found...");
				logger.info("Sending logged in event to mongo and lobby...");
				SimpleEventsProducer<LoggedInEvent> loggedInEventProducer = context.getBean(SimpleEventsProducer.class);
				loggedInEventProducer.setTopic(KafkaUtils.LOGGED_IN_EVENT_TOPIC);
				LoggedInEvent loggedInEvent = context.getBean(LoggedInEvent.class);
				loggedInEvent.setUuid(uuid);
				loggedInEvent.setBackgammonUser(logInUserAckEvent.getBackgammonUser());
				loggedInEvent.setArrived(new Date());
				loggedInEvent.setClazz("LoggedInEvent");
				loggedInEventProducer.sendKafkaMessage(loggedInEvent);
				logger.info("logged in event was sent successfuly to mongo and lobby...");
			}
			finally {
				return result;
			}
		}
	}
		
}
