package org.moshe.arad.controllers;

import java.util.Arrays;

import javax.validation.Valid;

import org.moshe.arad.entities.BackgammonUser;
import org.moshe.arad.entities.Status;
import org.moshe.arad.replies.IsUserFoundReply;
import org.moshe.arad.requests.UserCredentials;
import org.moshe.arad.services.HomeService;
import org.moshe.arad.validators.BackgammonUserValidator;
import org.moshe.arad.websocket.EmailAvailabilityMessage;
import org.moshe.arad.websocket.UserNameAvailabilityMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
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
	
	@RequestMapping(value = "/users/login", method = RequestMethod.GET)
	public ResponseEntity<IsUserFoundReply> findExistingUserAndLogin(@RequestParam String username, @RequestParam String password){
		HttpHeaders header = new HttpHeaders();
		header.add("Content-Type", "application/json");
		return new ResponseEntity<IsUserFoundReply>(homeService.findExistingUserAndLogin(new UserCredentials(username, password)), header, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/users/logout", method = RequestMethod.GET)
	public ResponseEntity<IsUserFoundReply> findExistingUserAndLogout(@RequestParam String username, @RequestParam String password){
		HttpHeaders header = new HttpHeaders();
		header.add("Content-Type", "application/json");
		return new ResponseEntity<IsUserFoundReply>(homeService.findExistingUserAndLogout(new UserCredentials(username, password)), header, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/users/", method = RequestMethod.POST)
	public ResponseEntity<String> createNewUser(@Valid @RequestBody BackgammonUser backgammonUser, Errors errors){
		if(errors.hasErrors()){
			logger.info("Some errors occured while trying to bind backgammon user");
			logger.info("There are " + errors.getErrorCount() + " errors.");
			
			for(FieldError error:errors.getFieldErrors()){
				logger.warn("Field name:  " + error.getField());
				logger.warn(error.getDefaultMessage());
			}
			
			if(!BackgammonUserValidator.acceptableErrors(errors)){
				logger.info("Routing for home page.");
				HttpHeaders header = new HttpHeaders();
				header.add("Content-Type", "application/json");
				return new ResponseEntity<String>(header, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		logger.info("The GameUser bind result: " + backgammonUser);
		
		try{
			
			boolean isCreated = homeService.createNewUser(backgammonUser);
			
			if(isCreated){
				HttpHeaders header = new HttpHeaders();
				header.add("Content-Type", "application/json");
				ObjectMapper mapper = new ObjectMapper();
				
				BackgammonUser user = new BackgammonUser(backgammonUser.getUserName(), backgammonUser.getPassword(), backgammonUser.getFirstName(), backgammonUser.getLastName(), backgammonUser.getEmail(), Status.LoggedIn, Arrays.asList("user"));
				return new ResponseEntity<String>(mapper.writeValueAsString(user), header, HttpStatus.CREATED);
			}
			else{
				HttpHeaders header = new HttpHeaders();
				header.add("Content-Type", "application/json");
				return new ResponseEntity<String>(header, HttpStatus.OK);
			}
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
	
	@RequestMapping(value = "/users/user_name/{userName}/", method = RequestMethod.GET)
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
	
	@RequestMapping(value = "/users/email/{email}/", method = RequestMethod.GET)
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
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		binder.addValidators(new BackgammonUserValidator());
	}
}
