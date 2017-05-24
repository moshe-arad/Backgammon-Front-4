package org.moshe.arad.controllers;

import java.io.IOException;

import org.moshe.arad.kafka.events.GetLobbyUpdateViewAckEvent;
import org.moshe.arad.replies.GameRoomsPayload;
import org.moshe.arad.replies.GetLobbyUpdateViewReply;
import org.moshe.arad.replies.IsGameRoomDeleted;
import org.moshe.arad.replies.IsGameRoomOpen;
import org.moshe.arad.replies.IsUserAddedAsWatcher;
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
	public ResponseEntity<IsGameRoomOpen> openNewGameRoom(@RequestBody String username){
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
	
	//to use it on game page when want to exit, but redirect to lobby, instead of logout.
	//TODO refactor this - do not use ack
	@RequestMapping(value = "/room/close", consumes="application/json", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<IsGameRoomDeleted> closeGameRoom(@RequestBody String username){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		ObjectMapper objectMapper = new ObjectMapper();
		String userNameFromJson = null;
		try {
			userNameFromJson = objectMapper.readTree(username).path("username").asText();
		} catch (IOException e) {
			e.printStackTrace();
		}
		IsGameRoomDeleted isGameRoomDeleted = lobbyService.closeGameRoomOpenedBy(userNameFromJson);
		return new ResponseEntity<IsGameRoomDeleted>(isGameRoomDeleted, headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/room/watcher", consumes = "application/json", method = RequestMethod.PUT, produces="application/json")
	public ResponseEntity<IsUserAddedAsWatcher> addWatcherToGameRoom(@RequestBody String body){
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
		IsUserAddedAsWatcher isUserAddedAsWatcher = lobbyService.addWatcherToGameRoom(userNameFromJson, gameRoomNameFromJson);
		return new ResponseEntity<IsUserAddedAsWatcher>(isUserAddedAsWatcher, headers, HttpStatus.OK);
	}
	
	// #### To view Service ####
	
	@RequestMapping(value = "/update/view", consumes="application/json", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<GetLobbyUpdateViewReply> getLobbyUpdateView(@RequestParam(required = false) String username){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		
		return new ResponseEntity<GetLobbyUpdateViewReply>(lobbyService.getLobbyUpdateView(username), headers, HttpStatus.OK);
	}
}
