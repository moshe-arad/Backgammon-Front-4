package org.moshe.arad.auditing;

import org.moshe.arad.entities.BackgammonUser;
import org.moshe.arad.repository.BackgammonUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

public class CustomAuditorAware implements AuditorAware<String> {

	@Autowired
	BackgammonUserRepository backgammonUserRepository;
	
	@Override
	public String getCurrentAuditor() {
		String loggedName = SecurityContextHolder.getContext().getAuthentication().getName();
		if(loggedName == null) return "System";
		else return loggedName;
	}

}
