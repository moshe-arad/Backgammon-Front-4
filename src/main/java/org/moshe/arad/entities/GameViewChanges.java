package org.moshe.arad.entities;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class GameViewChanges {

	private String messageToWhite;
	private String messageToBlack;
	private Boolean isToShowRollDiceBtnToWhite;
	private Boolean isToShowRollDiceBtnToBlack;
	private Boolean isWhiteTurn;
	private Boolean isBlackTurn;
	
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

	public Boolean getIsToShowRollDiceBtnToWhite() {
		return isToShowRollDiceBtnToWhite;
	}

	public void setIsToShowRollDiceBtnToWhite(Boolean isToShowRollDiceBtnToWhite) {
		this.isToShowRollDiceBtnToWhite = isToShowRollDiceBtnToWhite;
	}

	public Boolean getIsToShowRollDiceBtnToBlack() {
		return isToShowRollDiceBtnToBlack;
	}

	public void setIsToShowRollDiceBtnToBlack(Boolean isToShowRollDiceBtnToBlack) {
		this.isToShowRollDiceBtnToBlack = isToShowRollDiceBtnToBlack;
	}

	public Boolean getIsWhiteTurn() {
		return isWhiteTurn;
	}

	public void setIsWhiteTurn(Boolean isWhiteTurn) {
		this.isWhiteTurn = isWhiteTurn;
	}

	public Boolean getIsBlackTurn() {
		return isBlackTurn;
	}

	public void setIsBlackTurn(Boolean isBlackTurn) {
		this.isBlackTurn = isBlackTurn;
	}
}
