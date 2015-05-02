package it.polimi.model.carta;

import it.polimi.model.exceptions.IllegalTipoCartaSettore;
import it.polimi.model.player.Player;

public enum TipoCartaSettore {
	RUMORE_MIO,RUMORE_QUALUNQUE,SILENZIO;
	
	private void effettoRumoreMio(Player player){
		player.annunciaSettoreMio();
	}
	
	private void effettoRumoreQualunque(Player player){
		player.annunciaSettore();
	}
	
	private void effettoSilenzio(Player player){
		player.dichiaraSilenzio();
	}
	
	public void effetto(Player player){
		switch(this){
			case RUMORE_MIO:
				this.effettoRumoreMio(player);
			case RUMORE_QUALUNQUE:
				this.effettoRumoreQualunque(player);
			case SILENZIO:
				this.effettoSilenzio(player);
			default:
				throw new IllegalTipoCartaSettore(String.format("%s non è un tipo valido di Carta Settore", this.toString()));
		}
	}
}
