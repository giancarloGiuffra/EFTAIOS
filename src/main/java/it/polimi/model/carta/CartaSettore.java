package it.polimi.model.carta;

import java.util.ArrayList;
import java.util.List;

import it.polimi.model.player.Player;

/**
 * Classe per rappresentare la carta settore
 *
 */
public class CartaSettore implements Carta {

	private final TipoCartaSettore tipo;
	private final Boolean iconaOgetto;
	
	/**
	 * Costruttore
	 * @param tipo di carta settore
	 * @param iconaOggetto se ha o meno l'icon oggetto
	 */
	private CartaSettore(TipoCartaSettore tipo, Boolean iconaOggetto){
		this.tipo = tipo;
		this.iconaOgetto = iconaOggetto;
	}
	
	/**
	 * @return tipo di carta settore
	 */
	public TipoCartaSettore tipo() {
		return this.tipo;
	}
	
	/**
	 * @return true se ha l'icona oggetto, false altrimenti
	 */
	public Boolean hasIconaOgetto() {
		return this.iconaOgetto;
	}
	
	/**
	 * Restituisce una lista di carte della quantità e del tipo de carta settore indicati
	 * @param tipo di carta settore
	 * @param quantita di carte
	 * @return lista di carte
	 */
	public static List<Carta> getListCarteSettoreDiTipo(TipoCartaSettore tipo, int quantita){
	    Boolean defaultIconaOggetto = false;
	    List<Carta> listaCarteSettore = new ArrayList<Carta>(quantita);
	    for(int i=0; i<quantita;i++){
	        listaCarteSettore.add(new CartaSettore(tipo,defaultIconaOggetto));
	    }
	    return listaCarteSettore;
	}

	/**
	 * Effetto che la carta a sul giocatore
	 * @param giocatore sul quale applicare l'effetto
	 */
	@Override
	public void effetto(Player player) {
		this.tipo.effetto(player);
	}

}
