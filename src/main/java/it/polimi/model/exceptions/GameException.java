package it.polimi.model.exceptions;

public class GameException extends RuntimeException{
    
	private final String msg;
	
	/**
	 * Costruttore
	 * @param message
	 */
	public GameException(String message){
        this.msg = message;
    }
	
	/**
	 * @return messaggio con il quale è stata creata l'exception
	 */
	public String getMsg(){
		return this.msg;
	}
}
