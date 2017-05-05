package org.moshe.arad.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.moshe.arad.entities.BackgammonUser;
import org.moshe.arad.entities.BackgammonUserDetails;
import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.commands.LogOutUserCommand;
import org.moshe.arad.kafka.producers.commands.SimpleCommandsProducer;
import org.moshe.arad.repository.IBackgammonUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler{

	@Autowired
	private SimpleCommandsProducer<LogOutUserCommand> logOutUserCommandProducer;
	
	@Autowired
	private LogOutUserCommand logOutUserCommand;
	
	@Autowired
	private IBackgammonUserRepository backgammonUserRepository;
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		
		BackgammonUserDetails backgammonUserDetails = backgammonUserRepository.findByUserName(authentication.getName());
		BackgammonUser backgammonUser = new BackgammonUser(backgammonUserDetails.getUserName(), backgammonUserDetails.getPassword(), "", "", "", null);
		
		logOutUserCommand.setBackgammonUser(backgammonUser);
		
		logOutUserCommandProducer.setTopic(KafkaUtils.LOG_OUT_USER_COMMAND_TOPIC);
		logOutUserCommandProducer.sendKafkaMessage(logOutUserCommand);
		super.onLogoutSuccess(request, response, authentication);
	}

	
}
