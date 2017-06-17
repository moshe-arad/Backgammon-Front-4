package org.moshe.arad.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.moshe.arad.entities.GameRoom;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class GameViewChanges {

	private String messageToWhite;
	private String messageToBlack;
	
	public GameViewChanges() {
	
	}

	public GameViewChanges(String messageToWhite, String messageToBlack) {
		super();
		this.messageToWhite = messageToWhite;
		this.messageToBlack = messageToBlack;
	}

	@Override
	public String toString() {
		return "GameViewChanges [messageToWhite=" + messageToWhite + ", messageToBlack=" + messageToBlack + "]";
	}

	public String getMessageToWhite() {
		return messageToWhite;
	}

	public void setMessageToWhite(String messageToWhite) {
		this.messageToWhite = messageToWhite;
	}

	public String getMessageToBlack() {
		return messageToBlack;
	}

	public void setMessageToBlack(String messageToBlack) {
		this.messageToBlack = messageToBlack;
	}
}
