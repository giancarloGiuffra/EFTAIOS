package it.polimi.model.player;

import it.polimi.model.carta.Carta;
import it.polimi.model.carta.Mazzo;
import it.polimi.model.sector.Settore;

abstract public class Player {
    
    protected final Personaggio personaggio;
    private Mazzo mazzo;
    
    Player(Personaggio personaggio){this.personaggio=personaggio;}
    
    abstract public boolean isMoveValid(Settore from, Settore to);

	public Personaggio getPersonaggio() {
		return personaggio;
	}

	public void annunciaSettoreMio() {
		// TODO Auto-generated method stub
		
	}

	public void annunciaSettore() {
		// TODO Auto-generated method stub
		
	}

	public void dichiaraSilenzio() {
		// TODO Auto-generated method stub
		
	}
	
	public void usaCarta(Carta carta){
		carta.effetto(this);
	}
	
	public void muore(){
		//TODO
	}
	
	public void pescaCarta(Mazzo mazzo){
		//TODO
	}
}
