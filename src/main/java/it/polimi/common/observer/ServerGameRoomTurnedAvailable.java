package it.polimi.common.observer;

public class ServerGameRoomTurnedAvailable extends Event {

	/**
	 * Costruttore
	 */
	public ServerGameRoomTurnedAvailable(){
		this("Si sta chiudendo una game room");
	}
	
	/**
	 * Costruttore
	 * @param msg
	 */
	private ServerGameRoomTurnedAvailable(String msg) {
		super(msg);
	}

}
