package org.moshe.arad.security;

import org.moshe.arad.repository.BackgammonUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class BackgammonUserDetailsService implements UserDetailsService{

	@Autowired
	private BackgammonUserRepository backgammonUserRepository;
	
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		return backgammonUserRepository.findByUserName(userName);		
	}
		
}
