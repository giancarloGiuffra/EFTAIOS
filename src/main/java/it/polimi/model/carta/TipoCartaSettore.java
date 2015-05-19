package it.polimi.model.carta;

import it.polimi.model.exceptions.TipoCartaSettoreNonRiconsociuto;
import it.polimi.model.player.AzioneGiocatore;

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
	
	/**
	 * Restituisce il nome ufficiale del tipo di carta
	 * @return
	 */
	public String nome(){
	    switch(this){
    	    case RUMORE_MIO:
    	        return new StringBuilder().append("RUMORE NEL TUO SETTORE").toString();
    	    case RUMORE_QUALUNQUE:
    	        return new StringBuilder().append("RUMORE IN QUALUNQUE SETTORE").toString();
    	    case SILENZIO:
    	        return new StringBuilder().append("SILENZIO").toString();
	        default:
	            throw new TipoCartaSettoreNonRiconsociuto("Tipo di Carta Settore Sconosciuto"); 
	    }
	}
	
}