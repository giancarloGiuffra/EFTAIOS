package it.polimi.model.carta;

import it.polimi.model.exceptions.IllegalTipoCartaSettore;
import it.polimi.model.player.AzioneGiocatore;
import it.polimi.model.player.Player;

/**
 * Enumeration type per rappresentare il tipo di carta settore
 *
 */
public enum TipoCartaSettore {
	RUMORE_MIO(AzioneGiocatore.ANNUNCIA_SETTORE_MIO),
	RUMORE_QUALUNQUE(AzioneGiocatore.ANNUNCIA_SETTORE),
	SILENZIO(AzioneGiocatore.DICHIARA_SILENZIO);
	
	private final AzioneGiocatore azione;
	
	/**
	 * Costruttore
	 * @param azione che corrisponde al tipo di carta
	 */
	private TipoCartaSettore(AzioneGiocatore azione){
	    this.azione = azione;
	}
	
	/**
	 * @return azione che produce l'utilizzo di questo tipo di carta
	 */
	public AzioneGiocatore azione() {
        return this.azione;
    }
	
}
