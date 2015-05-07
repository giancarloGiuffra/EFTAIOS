package it.polimi.common.observer;

public final class UserTurnoFinitoEvent extends Event {

	/**
	 * Costruttore
	 */
	public UserTurnoFinitoEvent(){
		this("Il Giocatore ha finito il turno");
	}
	
	/**
	 * Costruttore
	 * @param msg
	 */
	private UserTurnoFinitoEvent(String msg) {
		super(msg);
	}

}
