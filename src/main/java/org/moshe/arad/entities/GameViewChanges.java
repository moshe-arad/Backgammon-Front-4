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
	private Boolean isWhiteReturned;
	private Boolean isBlackReturned;
	private Boolean isWhiteTookOut;
	private Boolean isBlackTookOut;
	private Boolean isBlackAteWhite;
	private Boolean isWhiteAteBlack;
	private Boolean isToApplyMove;
	
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

	public Boolean getIsWhiteReturned() {
		return isWhiteReturned;
	}

	public void setIsWhiteReturned(Boolean isWhiteReturned) {
		this.isWhiteReturned = isWhiteReturned;
	}

	public Boolean getIsBlackReturned() {
		return isBlackReturned;
	}

	public void setIsBlackReturned(Boolean isBlackReturned) {
		this.isBlackReturned = isBlackReturned;
	}

	public Boolean getIsWhiteTookOut() {
		return isWhiteTookOut;
	}

	public void setIsWhiteTookOut(Boolean isWhiteTookOut) {
		this.isWhiteTookOut = isWhiteTookOut;
	}

	public Boolean getIsBlackTookOut() {
		return isBlackTookOut;
	}

	public void setIsBlackTookOut(Boolean isBlackTookOut) {
		this.isBlackTookOut = isBlackTookOut;
	}

	public Boolean getIsBlackAteWhite() {
		return isBlackAteWhite;
	}

	public void setIsBlackAteWhite(Boolean isBlackAteWhite) {
		this.isBlackAteWhite = isBlackAteWhite;
	}

	public Boolean getIsWhiteAteBlack() {
		return isWhiteAteBlack;
	}

	public void setIsWhiteAteBlack(Boolean isWhiteAteBlack) {
		this.isWhiteAteBlack = isWhiteAteBlack;
	}

	public Boolean getIsToApplyMove() {
		return isToApplyMove;
	}

	public void setIsToApplyMove(Boolean isToApplyMove) {
		this.isToApplyMove = isToApplyMove;
	}
}
