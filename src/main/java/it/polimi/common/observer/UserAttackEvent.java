package it.polimi.common.observer;

public final class UserAttackEvent extends Event {

	/**
	 * Costruttore
	 */
	public UserAttackEvent(){
		this("Il Giocatora attacca.");
	}
	
	/**
	 * Costruttore
	 * @param msg
	 */
	private UserAttackEvent(String msg) {
		super(msg);
	}

}
