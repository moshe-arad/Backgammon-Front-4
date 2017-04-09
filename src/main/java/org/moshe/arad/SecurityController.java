package org.moshe.arad;

import java.security.Principal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityController {

	@RequestMapping("/authenticateUser")
	public Principal authenticateUser(Principal user){
		return user;
	}
}
