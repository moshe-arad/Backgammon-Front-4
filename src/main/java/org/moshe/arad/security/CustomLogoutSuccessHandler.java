package org.moshe.arad.security;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.moshe.arad.entities.BackgammonUser;
import org.moshe.arad.entities.BackgammonUserDetails;
import org.moshe.arad.kafka.EventsPool;
import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.commands.LogOutUserCommand;
import org.moshe.arad.kafka.events.LogOutUserAckEvent;
import org.moshe.arad.kafka.events.LoggedOutEvent;
import org.moshe.arad.kafka.producers.commands.SimpleCommandsProducer;
import org.moshe.arad.kafka.producers.events.SimpleEventsProducer;
import org.moshe.arad.repository.IBackgammonUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler{

	@Autowired
	private SimpleCommandsProducer<LogOutUserCommand> logOutUserCommandProducer;
	
	@Autowired
	private LogOutUserCommand logOutUserCommand;
	
	@Autowired
	private SimpleEventsProducer<LoggedOutEvent> loggedOutEventProducer;
	
	@Autowired
	private IBackgammonUserRepository backgammonUserRepository;
	
	@Autowired
	private EventsPool eventsPool;
	
	@Autowired
	private ApplicationContext context;
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		
		BackgammonUserDetails backgammonUserDetails = backgammonUserRepository.findByUserName(authentication.getName());
		BackgammonUser backgammonUser = new BackgammonUser(backgammonUserDetails.getUserName(), backgammonUserDetails.getPassword(), "", "", "", null);
		
		logOutUserCommand.setBackgammonUser(backgammonUser);
		
		logOutUserCommandProducer.setTopic(KafkaUtils.LOG_OUT_USER_COMMAND_TOPIC);
		UUID uuid = logOutUserCommandProducer.sendKafkaMessage(logOutUserCommand);
		
		eventsPool.getUserLogOutLockers().put(uuid.toString(), Thread.currentThread());
		
		synchronized (Thread.currentThread()) {
			
			try {
				Thread.currentThread().wait(5000);
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
		}
		
		super.onLogoutSuccess(request, response, authentication);		
	}

	
}
