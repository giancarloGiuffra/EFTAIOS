package it.polimi.model.carta;

import it.polimi.model.exceptions.IllegalTipoCartaSettore;
import it.polimi.model.player.Player;

/**
 * Enumeration type per rappresentare il tipo di carta settore
 *
 */
public enum TipoCartaSettore {
	RUMORE_MIO,RUMORE_QUALUNQUE,SILENZIO;
	
	/**
	 * Chiama l'azione associata all'effetto della carta RUMORE_MIO
	 * @param player di cui si chiama l'azione
	 */
	private void effettoRumoreMio(Player player){
		player.annunciaSettoreMio();
	}
	
	/**
     * Chiama l'azione associata all'effetto della carta RUMORE_QUALUNQUE
     * @param player di cui si chiama l'azione
     */
	private void effettoRumoreQualunque(Player player){
		player.annunciaSettore();
	}
	
	/**
     * Chiama l'azione associata all'effetto della carta SILENZIO
     * @param player di cui si chiama l'azione
     */
	private void effettoSilenzio(Player player){
		player.dichiaraSilenzio();
	}
	
	/**
	 * Chiama l'effetto corretto a seconda del tipo di carta settore
	 * @param player su cui applicare l'effetto
	 */
	public void effetto(Player player){
		switch(this){
			case RUMORE_MIO:
				this.effettoRumoreMio(player);
				break;
			case RUMORE_QUALUNQUE:
				this.effettoRumoreQualunque(player);
				break;
			case SILENZIO:
				this.effettoSilenzio(player);
				break;
			default:
				throw new IllegalTipoCartaSettore(String.format("%s non è un tipo valido di Carta Settore", this.toString()));
		}
	}
}
