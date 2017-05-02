package org.moshe.arad.auditing;

import org.moshe.arad.entities.BackgammonUserDetails;
import org.moshe.arad.repository.IBackgammonUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

public class CustomAuditorAware implements AuditorAware<String> {

	private static final String DEFAULT_AUDITOR = "System";
	
	@Autowired
	private IBackgammonUserRepository backgammonUserRepository;
	
	@Override
	public String getCurrentAuditor() {
		String loggedName = SecurityContextHolder.getContext().getAuthentication().getName();
		if(loggedName == null) return DEFAULT_AUDITOR;
		else return loggedName;
	}

}
