package org.moshe.arad.kafka.events;

import org.moshe.arad.entities.GameViewChanges;
import org.moshe.arad.entities.UsersViewChanges;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class GetGameUpdateViewAckEvent extends BackgammonEvent{
	
	private GameViewChanges gameViewChange;
	
	public GetGameUpdateViewAckEvent() {
	
	}

	public GetGameUpdateViewAckEvent(GameViewChanges gameViewChange) {
		super();
		this.gameViewChange = gameViewChange;
	}

	@Override
	public String toString() {
		return "GetGameUpdateViewAckEvent [gameViewChange=" + gameViewChange + "]";
	}

	public GameViewChanges getGameViewChange() {
		return gameViewChange;
	}

	public void setGameViewChange(GameViewChanges gameViewChange) {
		this.gameViewChange = gameViewChange;
	}
}
