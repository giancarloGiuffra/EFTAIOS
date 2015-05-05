package it.polimi.common.observer;

/**
 * Classe per gli Eventi da lanciare
 *
 */
abstract public class Event {
	private final String msg;
	
	/**
	 * Costruttore
	 * @param msg
	 */
	private Event(String msg){
		this.msg = msg;
	}
	
	/**
	 * @return messaggio che caratterizza l'evento
	 */
	public String getMsg(){
		return this.msg;
	}
}
