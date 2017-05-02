package org.moshe.arad.validators;

import org.moshe.arad.entities.BackgammonUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;

public class BackgammonUserValidator implements Validator, IEntityValidator {

	private final static Logger logger = LoggerFactory.getLogger(BackgammonUserValidator.class);
	
	@Override
	public boolean supports(Class<?> clazz) {
		return BackgammonUserDetails.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {	
	}
	
	public static boolean acceptableErrors(Errors errors) {
		for(FieldError error:errors.getFieldErrors()){
			if(!error.getField().equals("enabled") && !ignore.contains(error.getField())){
				logger.info("This error couldn't be ignore " + error);
				return false;
			}
			else logger.info("Ignoring error " + error);
		}
		return true;
	}
}
