package it.polimi.model.carta;

import it.polimi.model.player.*;

/**
 * Classe per rappresentare le carte
 *
 */
public class Carta {
    
    private final AzioneGiocatore azione;
    
    Carta(AzioneGiocatore azione){
        this.azione=azione;
    }
    
    /**
     * Effetto della carta sul giocatore
     * @param player su cui applicare l'effetto
     * @deprecated si veda metodo azione
     */
    @Deprecated
	public void effetto(Player player){}

    public AzioneGiocatore azione() {
        return this.azione;
    }
    
    
}
