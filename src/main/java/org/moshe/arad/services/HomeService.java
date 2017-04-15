package org.moshe.arad.services;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.moshe.arad.entities.BackgammonUser;
import org.moshe.arad.repository.BackgammonUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class HomeService {

	private Logger logger = LoggerFactory.getLogger(HomeService.class);

	@Autowired
	private BackgammonUserRepository backgammonUserRepository;
	
	public void registerNewUser(BackgammonUser backgammonUser) {
		
		if(!isEmailAvailable(backgammonUser.getEmail()) || !isUserNameAvailable(backgammonUser.getUsername())) throw new RuntimeException("Email or user name is already taken.");
		logger.info("User's email and user name are available.");
		backgammonUser.setEnabled(true);
		backgammonUserRepository.save(backgammonUser);
		authenticateUser(backgammonUser);
		logger.info("User saved to local DB, and authenticated.");
	}	
	
	public boolean isUserNameAvailable(String userName){		
		List<String> userNames = backgammonUserRepository.findAll().stream().map(backgammonUser -> backgammonUser.getEmail()).collect(Collectors.toList());
		return !userNames.contains(userName);
	}
	
	public boolean isEmailAvailable(String email){
		List<String> emails = backgammonUserRepository.findAll().stream().map(backgammonUser -> backgammonUser.getEmail()).collect(Collectors.toList());
		return !emails.contains(email);
	}
	
	private void authenticateUser(BackgammonUser backgammonUser) {
		Authentication auth = new UsernamePasswordAuthenticationToken(backgammonUser, 
				backgammonUser.getPassword(), backgammonUser.getAuthorities()); 
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
}



















