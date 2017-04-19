package org.moshe.arad.controllers;

import javax.validation.Valid;

import org.moshe.arad.entities.BackgammonUser;
import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.commands.Commandable;
import org.moshe.arad.kafka.commands.CreateNewUserCommand;
import org.moshe.arad.kafka.producers.CreateNewUserCommandProducer;
import org.moshe.arad.kafka.producers.SimpleProducer;
import org.moshe.arad.services.HomeService;
import org.moshe.arad.validators.BackgammonUserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
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
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = "/users")
public class UsersController implements ApplicationContextAware{
	
	private final Logger logger = LoggerFactory.getLogger(UsersController.class);
	
	private ApplicationContext context;
	
	@Autowired
	private HomeService homeService;
	
	private SimpleProducer simpleProducer;
	
	private Commandable command;
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
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
			homeService.registerNewUser(backgammonUser);
			
			command = context.getBean(CreateNewUserCommand.class);
			((CreateNewUserCommand)command).setBackgammonUser(backgammonUser);
			
			simpleProducer = context.getBean(CreateNewUserCommandProducer.class);
			simpleProducer.sendKafkaMessage(KafkaUtils.COMMANDS_TO_USERS_SERVICE_TOPIC, command);
			
			HttpHeaders header = new HttpHeaders();
			header.add("Content-Type", "application/json");
			ObjectMapper mapper = new ObjectMapper();
			return new ResponseEntity<String>(mapper.writeValueAsString(backgammonUser), header, HttpStatus.CREATED);
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
	
	@RequestMapping(value = "/user_name/{userName}", method = RequestMethod.GET)
	public ResponseEntity<String> isUserNameAvailable(@PathVariable String userName){
		try{
			logger.info("User name bind result: " + userName);
			if(homeService.isUserNameAvailable(userName)){
				logger.info("User name available for registeration.");
				HttpHeaders headers = new HttpHeaders();
				headers.add("Content-Type", "text/html");
				return new ResponseEntity<String>("", headers,HttpStatus.OK);
				
			}
			else {
				logger.info("User name not available can't register.");
				HttpHeaders headers = new HttpHeaders();
				headers.add("Content-Type", "text/html");
				return new ResponseEntity<String>("User name is not available.", headers,HttpStatus.OK);
			}
		}
		catch (Exception ex) {
			logger.error(ex.getMessage());
			logger.error(ex.toString());
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "text/html");
			return new ResponseEntity<String>("Ajax call encountred a server error.", headers,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/email/{email}/", method = RequestMethod.GET)
	public ResponseEntity<String> isUserEmailAvailable(@PathVariable String email){
		try{
			logger.info("Email bind result: " + email);
			if(homeService.isEmailAvailable(email)){
				logger.info("Email available for registeration.");
				HttpHeaders headers = new HttpHeaders();
				headers.add("Content-Type", "text/html");
				return new ResponseEntity<>("", headers, HttpStatus.OK);
			}
			else{
				logger.info("Email not available can't register.");
				HttpHeaders headers = new HttpHeaders();
				headers.add("Content-Type", "text/html");
				return new ResponseEntity<>("Email is not available.", headers, HttpStatus.OK);
			}
		}
		catch(Exception ex){
			logger.error(ex.getMessage());
			logger.error(ex.toString());
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "text/html");
			return new ResponseEntity<String>("Ajax call encountred a server error.", headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		binder.addValidators(new BackgammonUserValidator());
	}

	@Override
	public void setApplicationContext(ApplicationContext appContext) throws BeansException {
		this.context = appContext;
	}
}
