package it.polimi.common.observer;

public final class UserPicksCardEvent extends Event {

	/**
	 * Costruttore
	 */
	public UserPicksCardEvent(){
		this("Il Giocatore pesca una carta");
	}
	
	/**
	 * Costruttore
	 * @param msg
	 */
	private UserPicksCardEvent(String msg) {
		super(msg);
	}

}
