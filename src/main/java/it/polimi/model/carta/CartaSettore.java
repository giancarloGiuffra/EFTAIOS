package it.polimi.model.carta;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe per rappresentare la carta settore
 *
 */
public class CartaSettore extends Carta {

	private final TipoCartaSettore tipo;
	private final Boolean iconaOgetto;
	
	/**
	 * Costruttore
	 * @param tipo di carta settore
	 * @param iconaOggetto se ha o meno l'icon oggetto
	 */
	private CartaSettore(TipoCartaSettore tipo, Boolean iconaOggetto){
		super(tipo.azione());
	    this.tipo = tipo;
		this.iconaOgetto = iconaOggetto;
	}
	
	/**
	 * Copy COnstructor
	 * @param source
	 */
	public CartaSettore(CartaSettore source){
		super((Carta)source); //non da errore se non si fa il cast, ma meglio essere espliciti
		this.tipo = source.tipo;
		this.iconaOgetto = new Boolean(source.iconaOgetto);
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
	 * @return il nome ufficiale del tipo di carta settore
	 */
	@Override
	public String nome(){
	    return this.tipo.nome();
	}

}
