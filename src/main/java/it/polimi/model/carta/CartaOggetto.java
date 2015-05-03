package it.polimi.model.carta;

import it.polimi.model.player.AzioneGiocatore;
import it.polimi.model.player.Player;

/**
 * Classe per rappresentare la carta oggetto
 *
 */
public class CartaOggetto extends Carta {

	
	CartaOggetto(AzioneGiocatore azione){
	    super(azione);
	}
    
    @Deprecated
	public void effetto(Player player) {
		// TODO Auto-generated method stub

	}

}
