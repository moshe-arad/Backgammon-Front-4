package org.moshe.arad.security;

import java.util.UUID;

import org.moshe.arad.entities.BackgammonUser;
import org.moshe.arad.kafka.EventsPool;
import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.commands.LogInUserCommand;
import org.moshe.arad.kafka.producers.commands.SimpleCommandsProducer;
import org.moshe.arad.repository.IBackgammonUserRepository;
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
	
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		UserDetails result = backgammonUserRepository.findByUserName(userName);
		
		//will update view as user loggedIn
		SimpleCommandsProducer<LogInUserCommand> logInUserCommandProducer = context.getBean(SimpleCommandsProducer.class);
		logInUserCommandProducer.setTopic(KafkaUtils.LOG_IN_USER_COMMAND_TOPIC);
		LogInUserCommand logInUserCommand = context.getBean(LogInUserCommand.class);
		logInUserCommand.setUser((BackgammonUser)result);
		
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
		
		
	}
		
}
