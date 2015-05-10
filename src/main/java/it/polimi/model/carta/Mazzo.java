package it.polimi.model.carta;

import it.polimi.model.exceptions.MazzoVuotoException;

import java.util.ArrayDeque;
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
        this();
        Collections.shuffle(lista);
        this.carte.addAll(lista);
    }
    
    public Mazzo(){
    	this.carte = new ArrayDeque<Carta>();
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
	public boolean isEmpty(){
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
	 * Aggiunge a questo mazzo le carte dell'altro mazzo
	 * @param other
	 */
	public void addMazzo(Mazzo other){
		this.carte.addAll(other.carte);
	}
	
	/**
	 * Mischia le carte del mazzo
	 */
	public void rimischia(){
		if(!this.isEmpty()){
			List<Carta> listaCarte = new ArrayList<Carta>();
			listaCarte.addAll(this.carte);
			this.carte.removeAll(listaCarte);
			Collections.shuffle(listaCarte);
			this.carte.addAll(listaCarte);
		}
	}
}
