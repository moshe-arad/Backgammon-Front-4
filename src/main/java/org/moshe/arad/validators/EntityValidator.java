package org.moshe.arad.validators;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

public interface EntityValidator {

	public final static Logger logger = LoggerFactory.getLogger(EntityValidator.class);
	
	public static final Set<String> ignore = new HashSet<>(Arrays.asList(
			"createdDate","lastModifiedBy","createdBy","lastModifiedDate"
			));
	
	public static boolean acceptableErrors(Errors errors) {
		for(FieldError error:errors.getFieldErrors()){
			if(!ignore.contains(error.getField())){
				logger.info("This error couldn't be ignore " + error);
				return false;
			}
			else logger.info("Ignoring error " + error);
		}
		return true;
	}
}
