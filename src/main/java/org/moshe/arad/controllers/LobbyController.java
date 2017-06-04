package org.moshe.arad.controllers;

import java.io.IOException;

import org.moshe.arad.replies.GetLobbyUpdateViewReply;
import org.moshe.arad.services.LobbyService;
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
@RequestMapping("/lobby")
public class LobbyController {

	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private LobbyService lobbyService;
	
	@RequestMapping(value = "/room", consumes="application/json", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<?> openNewGameRoom(@RequestBody String username){
		HttpHeaders header = new HttpHeaders();
		header.add("Content-Type", "application/json");
		ObjectMapper objectMapper = new ObjectMapper();
		String userNameFromJson = null;
		try {
			userNameFromJson = objectMapper.readTree(username).path("username").asText();
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
		lobbyService.openNewGameRoom(userNameFromJson);
		return new ResponseEntity<>(header, HttpStatus.CREATED);
		
	}
	
	@RequestMapping(value = "/room/watcher", consumes = "application/json", method = RequestMethod.PUT, produces="application/json")
	public ResponseEntity<?> addWatcherToGameRoom(@RequestBody String body){
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
		lobbyService.addWatcherToGameRoom(userNameFromJson, gameRoomNameFromJson);
		return new ResponseEntity<>(headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/room/second/player", consumes = "application/json", method = RequestMethod.PUT, produces="application/json")
	public ResponseEntity<?> addSecondPlayerToGameRoom(@RequestBody String body){
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
		lobbyService.addSecondPlayerToGameRoom(userNameFromJson, gameRoomNameFromJson);
		return new ResponseEntity<>(headers, HttpStatus.OK);
	}
	
	// #### To view Service ####
	
	@RequestMapping(value = "/room/all", consumes="application/json", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> getAllGameRooms(@RequestParam String username){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
	
		lobbyService.getAllGameRooms(username);
		return new ResponseEntity<>(headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/update/view", consumes="application/json", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<GetLobbyUpdateViewReply> getLobbyUpdateView(@RequestParam String all, @RequestParam String group, @RequestParam String user){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		
		return new ResponseEntity<GetLobbyUpdateViewReply>(lobbyService.getLobbyUpdateView(all, group, user), headers, HttpStatus.OK);
	}
}
