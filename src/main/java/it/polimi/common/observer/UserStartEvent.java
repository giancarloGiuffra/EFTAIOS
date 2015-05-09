package it.polimi.common.observer;

public final class UserStartEvent extends Event {

	/**
	 * Csotruttore
	 */
	public UserStartEvent(){
		this("Il Giocatore ha iniziato il gioco.");
	}
	
	/**
	 * Costruttore
	 * @param msg
	 */
	private UserStartEvent(String msg) {
		super(msg);
	}

}
