package it.polimi.common.observer;

import java.io.Serializable;

public class RMIEvent implements Serializable {

	private static final long serialVersionUID = 3694121898827191647L;
	private final String msg;

	/**
	 * Costruttore
	 * @param msg
	 */
	public RMIEvent(String msg){
		this.msg = msg;
	}
	
	/**
	 * @return messaggio che caratterizza l'evento
	 */
	public String getMsg(){
		return this.msg;
	}
	
	/**
	 * @return il nome dell'evento - dato dalla sua classe derivata
	 */
	public String name(){
		return this.getClass().getSimpleName();
	}

}
