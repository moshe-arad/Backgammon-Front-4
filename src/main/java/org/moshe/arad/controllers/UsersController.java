package org.moshe.arad.controllers;

import java.io.IOException;
import java.util.Arrays;

import org.moshe.arad.entities.BackgammonUser;
import org.moshe.arad.entities.Status;
import org.moshe.arad.kafka.events.GetUsersUpdateViewAckEvent;
import org.moshe.arad.replies.GetLobbyUpdateViewReply;
import org.moshe.arad.replies.IsUserFoundReply;
import org.moshe.arad.requests.UserCredentials;
import org.moshe.arad.services.HomeService;
import org.moshe.arad.websocket.EmailAvailabilityMessage;
import org.moshe.arad.websocket.UserNameAvailabilityMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class UsersController {
	
	private final Logger logger = LoggerFactory.getLogger(UsersController.class);
	
	@Autowired
	private HomeService homeService;
	
	@RequestMapping(value = "/users/login", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	public ResponseEntity<?> findExistingUserAndLogin(@RequestBody String body){
		HttpHeaders header = new HttpHeaders();
		header.add("Content-Type", "application/json");
		
		ObjectMapper objectMapper = new ObjectMapper();
		String userNameFromJson = null;
		String passwordFromJson = null;
		try {
			userNameFromJson = objectMapper.readTree(body).path("username").asText();
			passwordFromJson = objectMapper.readTree(body).path("password").asText();
		} catch (IOException e) {
			e.printStackTrace();
		}
		homeService.findExistingUserAndLogin(new UserCredentials(userNameFromJson, passwordFromJson));
		return new ResponseEntity<>(header, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/users/logout", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	public ResponseEntity<?> findExistingUserAndLogout(@RequestBody String body){
		HttpHeaders header = new HttpHeaders();
		header.add("Content-Type", "application/json");
		
		ObjectMapper objectMapper = new ObjectMapper();
		String userNameFromJson = null;
		String passwordFromJson = null;
		try {
			userNameFromJson = objectMapper.readTree(body).path("username").asText();
			passwordFromJson = objectMapper.readTree(body).path("password").asText();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		homeService.findExistingUserAndLogout(new UserCredentials(userNameFromJson, passwordFromJson));
		
		return new ResponseEntity<>(header, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/users/", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<String> createNewUser(@RequestBody BackgammonUser backgammonUser, Errors errors){
		logger.info("The GameUser bind result: " + backgammonUser);
		
		try{
			
			homeService.createNewUser(backgammonUser);
			
			HttpHeaders header = new HttpHeaders();
			header.add("Content-Type", "application/json");
			ObjectMapper mapper = new ObjectMapper();
			
			BackgammonUser user = new BackgammonUser(backgammonUser.getUserName(), backgammonUser.getPassword(), backgammonUser.getFirstName(), backgammonUser.getLastName(), backgammonUser.getEmail(), Status.LoggedIn, Arrays.asList("user"));
			return new ResponseEntity<String>(mapper.writeValueAsString(user), header, HttpStatus.CREATED);
		}
		catch(Exception ex){
			logger.info("User register failed.");
			logger.info("Routing for home page.");
			logger.error(ex.getMessage());
			logger.error(ex.toString());
			HttpHeaders header = new HttpHeaders();
			header.add("Content-Type", "application/json");
			return new ResponseEntity<String>(header, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// #### To view Service ####
	
	@RequestMapping(value = "/users/user_name/{userName}/", method = RequestMethod.GET, consumes = "application/json", produces = "application/json")
	public UserNameAvailabilityMessage isUserNameAvailable(@PathVariable String userName){
		boolean isAvailable = false;
		
		try{
			logger.info("User name bind result: " + userName);
			isAvailable = homeService.isUserNameAvailable(userName);
			if(isAvailable) logger.info("User name available for registeration.");
			else logger.info("User name not available can't register."); 				
			return new UserNameAvailabilityMessage(isAvailable);
		}
		catch (Exception ex) {
			logger.error(ex.getMessage());
			logger.error(ex.toString());
			return new UserNameAvailabilityMessage(false);
		}
	}
	
	@RequestMapping(value = "/users/email/{email}/", method = RequestMethod.GET, consumes = "application/json", produces = "application/json")
	public EmailAvailabilityMessage isUserEmailAvailable(@PathVariable String email){
		boolean isAvailable = false;
		
		try{
			logger.info("Email bind result: " + email);
			isAvailable = homeService.isEmailAvailable(email);
			if(isAvailable) logger.info("Email available for registeration.");
			else logger.info("Email not available can't register.");
			return new EmailAvailabilityMessage(isAvailable);
		}
		catch(Exception ex){
			logger.error(ex.getMessage());
			logger.error(ex.toString());
			return new EmailAvailabilityMessage(false);
		}
	}
	
	@RequestMapping(value = "/users/update/view", consumes="application/json", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<GetUsersUpdateViewAckEvent> getLobbyUpdateView(@RequestParam String all, @RequestParam String group, @RequestParam String user){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		
		GetUsersUpdateViewAckEvent result = homeService.getUsersUpdateView(all, group, user);
		
		return new ResponseEntity<GetUsersUpdateViewAckEvent>(result, headers, HttpStatus.OK);
	}
}
