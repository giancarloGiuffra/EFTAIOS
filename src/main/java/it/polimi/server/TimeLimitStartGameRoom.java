package it.polimi.server;

import java.util.TimerTask;

public class TimeLimitStartGameRoom extends TimerTask {

	private GameRoom gameRoom;
	
	/**
	 * Constructor
	 * @param gameRoom
	 */
	public TimeLimitStartGameRoom(GameRoom gameRoom){
		this.gameRoom = gameRoom;
	}
	
	@Override
	public void run() {
		if(this.gameRoom.canStart())
			this.gameRoom.start();
		else{
			this.gameRoom.clientManager().broadcast(String.format("Non è stato raggiunto il numero minimo di partecipanti (%d).\nLa connessione verrà chiusa.", this.gameRoom.clientManager().minimumNumber()));
			this.gameRoom.close();
		}
	}

}
