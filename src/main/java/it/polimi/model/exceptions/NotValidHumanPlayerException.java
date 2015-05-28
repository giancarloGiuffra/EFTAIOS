package it.polimi.model.exceptions;

public class NotValidHumanPlayerException extends GameException {

	public NotValidHumanPlayerException(){
		this("personaggio alieno è finito in HumanPlayer");
	}
	
	public NotValidHumanPlayerException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
