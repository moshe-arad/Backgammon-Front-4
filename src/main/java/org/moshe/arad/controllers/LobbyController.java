package org.moshe.arad.controllers;

import org.moshe.arad.replies.IsGameRoomOpen;
import org.moshe.arad.replies.IsUserFoundReply;
import org.moshe.arad.requests.UserCredentials;
import org.moshe.arad.services.LobbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lobby")
public class LobbyController {

	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private LobbyService lobbyService;
	
	@RequestMapping(value = "/room", consumes="application/json", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<IsGameRoomOpen> openNewGameRoom(@RequestBody String username){
		HttpHeaders header = new HttpHeaders();
		header.add("Content-Type", "application/json");
		return new ResponseEntity<IsGameRoomOpen>(lobbyService.openNewGameRoom(username), header, HttpStatus.CREATED);
	}
}
