package it.polimi.model.carta;

import java.util.ArrayList;
import java.util.List;

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
	
	public static List<Carta> getListCarteSettoreDiTipo(TipoCartaSettore tipo, int quantita){
	    Boolean defaultIconaOggetto = false;
	    List<Carta> listaCarteSettore = new ArrayList<Carta>(quantita);
	    for(int i=0; i<quantita;i++){
	        listaCarteSettore.add(new CartaSettore(tipo,defaultIconaOggetto));
	    }
	    return listaCarteSettore;
	}

	@Override
	public void effetto(Player player) {
		this.tipo.effetto(player);
	}

}
