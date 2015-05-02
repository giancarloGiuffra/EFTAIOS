package it.polimi.model.player;

import it.polimi.model.carta.Carta;
import it.polimi.model.carta.Mazzo;
import it.polimi.model.sector.Settore;

/**
 * Classe Astratta da cui derivare i giocatori.
 *
 */
abstract public class Player {
    
    protected final Personaggio personaggio;
    private Mazzo mazzo;
    
    /**
     * Costruttore
     * @param personaggio
     */
    Player(Personaggio personaggio){
        this.personaggio=personaggio;
    }
    
    /**
     *  Metodo per controllare se la mossa è valida
     * @param from  settore di partenza
     * @param to    settore di arrivo
     * @return true se la mossa è valida, false altrimenti
     */
    abstract public boolean isMoveValid(Settore from, Settore to);

	/**
	 * Restituisce il personaggio del giocatore
	 * @return personaggio
	 */
    public Personaggio getPersonaggio() {
		return personaggio;
	}

	/**
	 * Metodo per annunciare il settore in cui si trova il giocatore
	 */
    public void annunciaSettoreMio() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Metodo per chiedere un settore al giocatore e annunciarlo
	 */
    public void annunciaSettore() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Metodo per dichiarare silenzio
	 */
    public void dichiaraSilenzio() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Metodo per usare una carta
	 * @param carta
	 */
    public void usaCarta(Carta carta){
		carta.effetto(this);
	}
	
	/**
	 * Metodo per dichiarare di essere morto
	 */
    public void muore(){
		//TODO
	}
	
	/**
	 * Metodo per pescare una carta dal mazzo
	 * @param mazzo mazzo da cui pescare la carta
	 */
    public void pescaCarta(Mazzo mazzo){
		//TODO
	}
}
