package it.polimi.model.carta;

import it.polimi.model.exceptions.MazzoVuotoException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

/**
 * Classe per rappresentare un mazzo di carte
 *
 */
public class Mazzo {
	
	private Deque<Carta> carte;
	
	/**
     * Costruttore
     * @param lista di carte
     */
    private Mazzo(List<Carta> lista){
        Collections.shuffle(lista);
        this.carte.addAll(lista);
    }
	
	/**
	 * @return la prossima carta da estrarre
	 */
	public Carta getCarta(){
		if(this.isEmpty()) throw new MazzoVuotoException("Il Mazzo è vuoto");
		return carte.pop();
	}
	
	/**
	 * Inserisce la carta nel mazzo
	 * @param carta da inserire
	 */
	public void putCarta(Carta carta){
	    this.carte.push(carta);
	}
	
	/**
	 * @return true se il mazzo è vuoto, false altrimenti
	 */
	private boolean isEmpty(){
		return carte.isEmpty();
	}
	
	/**
	 * Restituisce un nuovo mazzo di carte con la distribuzione indicata
	 * @param distribuzione delle carte di tipo settore
	 * @return mazzo di carte di tipo settore con distribuzione indicata
	 */
	private static Mazzo creaNuovoMazzoCarteSettore(DistribuzioneCarteSettore distribuzione){
	    List<Carta> listaRumoreMio = CartaSettore.getListCarteSettoreDiTipo(TipoCartaSettore.RUMORE_MIO, distribuzione.rumoreMio());
	    List<Carta> listaRumoreQualunque = CartaSettore.getListCarteSettoreDiTipo(TipoCartaSettore.RUMORE_QUALUNQUE, distribuzione.rumoreQualunque());
	    List<Carta> listaSilenzio = CartaSettore.getListCarteSettoreDiTipo(TipoCartaSettore.SILENZIO, distribuzione.silenzio());
	    
	    List<Carta> lista = new ArrayList<Carta>();
	    lista.addAll(listaRumoreMio);
	    lista.addAll(listaRumoreQualunque);
	    lista.addAll(listaSilenzio);
	
	    return new Mazzo(lista);	    
	}
	
	/**
     * Restituisce un nuovo mazzo di carte equidistribuito (tra i tipi di carte settore)
     * @return mazzo di carte di tipo settore equidistribuito
     */
	public static Mazzo creaNuovoMazzoCarteSettore(){
	    return creaNuovoMazzoCarteSettore(DistribuzioneCarteSettore.EQUIDISTRIBUITA);
	}
	
	/**
	 * Aggiunge le carte al mazzo e rimischia
	 * Si pensa di utilizzarla quando il mazzo sia vuoto
	 * @param listaCarte lista di carte da aggiungere al mazzo
	 */
	public void rimischia(List<Carta> listaCarte){
	    Collections.shuffle(listaCarte);
        this.carte.addAll(listaCarte);
	}
}
