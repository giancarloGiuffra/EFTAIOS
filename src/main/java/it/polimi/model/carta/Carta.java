package it.polimi.model.carta;

import it.polimi.model.player.*;

/**
 * Interfaccia per le carte
 *
 */
public interface Carta {
    
    /**
     * Effetto della carta sul giocatore
     * @param player su cui applicare l'effetto
     */
	public void effetto(Player player);
}
