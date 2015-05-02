package it.polimi.model.carta;

import it.polimi.model.player.Player;

public class CartaSettore implements Carta {

	private final TipoCartaSettore tipo;
	private final Boolean iconaOgetto;
	
	private CartaSettore(TipoCartaSettore tipo, Boolean iconaOggetto){
		this.tipo = tipo; this.iconaOgetto = iconaOggetto;
	}
	
	public TipoCartaSettore tipo() {
		return this.tipo;
	}
	
	public Boolean hasIconaOgetto() {
		return this.iconaOgetto;
	}

	@Override
	public void effetto(Player player) {
		this.tipo.effetto(player);
	}

}
