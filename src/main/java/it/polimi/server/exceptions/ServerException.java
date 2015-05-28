package it.polimi.server.exceptions;

public class ServerException extends RuntimeException {

private final String msg;
	
	/**
	 * Costruttore
	 * @param message
	 */
	public ServerException(String message){
        this.msg = message;
    }
	
	/**
	 * @return messaggio con il quale è stata creata l'exception
	 */
	public String getMsg(){
		return this.msg;
	}

}
