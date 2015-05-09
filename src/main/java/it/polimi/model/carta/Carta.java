package it.polimi.model.carta;

import it.polimi.model.player.*;

/**
 * Classe per rappresentare le carte
 *
 */
public abstract class Carta {
    
    private final AzioneGiocatore azione;
    
    /**
     * Costruttore
     * @param azione
     */
    Carta(AzioneGiocatore azione){
        this.azione=azione;
    }

    /**
     * @return azione associata alla carta
     */
    public AzioneGiocatore azione() {
        return this.azione;
    }
    
    /**
     * @return nome ufficiale della carta
     */
    public abstract String nome();
    
    
}
