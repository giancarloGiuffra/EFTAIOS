package it.polimi.model.carta;

import it.polimi.model.exceptions.MazzoVuotoException;

import java.util.Stack;

public class Mazzo {
	
	private Stack<Carta> carte;
	
	public Carta getCarta(){
		if(this.isEmpty()) throw new MazzoVuotoException("Il Mazzo è vuoto");
		return carte.pop();
	}
	
	public boolean isEmpty(){
		return carte.empty();
	}
}
