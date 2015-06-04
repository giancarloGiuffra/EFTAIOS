package it.polimi.model.carta;

import it.polimi.model.player.AzioneGiocatore;

/**
 * Classe per rappresentare la carta oggetto
 *
 */
public class CartaOggetto extends Carta {

	
	/**
	 * Costruttore
	 * @param azione
	 */
    CartaOggetto(AzioneGiocatore azione){
	    super(azione);
	}

    @Override
    public String nome() {
        // TODO Auto-generated method stub
        return null;
    }  

}