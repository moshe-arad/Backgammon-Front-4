package org.moshe.arad.controllers;

import org.moshe.arad.entities.GameViewChanges;
import org.moshe.arad.replies.GetLobbyUpdateViewReply;
import org.moshe.arad.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
public class GameController {

	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private GameService gameService;
	
	@RequestMapping(value = "/update/view", consumes="application/json", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<GameViewChanges> getLobbyUpdateView(@RequestParam String all, @RequestParam String group, @RequestParam String user){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		
		return new ResponseEntity<GameViewChanges>(gameService.getGameUpdateView(all, group, user), headers, HttpStatus.OK);
	}
}
