package org.moshe.arad.controllers;

import java.io.IOException;

import org.moshe.arad.entities.GameViewChanges;
import org.moshe.arad.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/game")
public class GameController {

	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private GameService gameService;
	
	@RequestMapping(value = "/move", consumes="application/json", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<?> makeMove(@RequestBody String body){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		ObjectMapper objectMapper = new ObjectMapper();
		String userNameFromJson = null;
		String gameRoomNameFromJson = null;
		int fromColumnFromJson = -2;
		int toColumnFromJson = -2;
		
		try {
			userNameFromJson = objectMapper.readTree(body).path("username").asText();
			gameRoomNameFromJson = objectMapper.readTree(body).path("gameRoomName").asText();
			fromColumnFromJson = objectMapper.readTree(body).path("from").asInt();
			toColumnFromJson = objectMapper.readTree(body).path("to").asInt();
		} catch (IOException e) {
			e.printStackTrace();
		}
		gameService.makeMove(userNameFromJson, gameRoomNameFromJson, fromColumnFromJson, toColumnFromJson);
		return new ResponseEntity<>(headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/rollDice", consumes="application/json", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<?> rollDice(@RequestBody String body){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		ObjectMapper objectMapper = new ObjectMapper();
		String userNameFromJson = null;
		String gameRoomNameFromJson = null;
		try {
			userNameFromJson = objectMapper.readTree(body).path("username").asText();
			gameRoomNameFromJson = objectMapper.readTree(body).path("gameRoomName").asText();
		} catch (IOException e) {
			e.printStackTrace();
		}
		gameService.rollDice(userNameFromJson, gameRoomNameFromJson);
		return new ResponseEntity<>(headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/update/view", consumes="application/json", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<GameViewChanges> getLobbyUpdateView(@RequestParam String all, @RequestParam String group, @RequestParam String user){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		
		return new ResponseEntity<GameViewChanges>(gameService.getGameUpdateView(all, group, user), headers, HttpStatus.OK);
	}
}
