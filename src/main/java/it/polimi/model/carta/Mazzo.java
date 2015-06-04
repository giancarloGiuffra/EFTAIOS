package it.polimi.model.carta;

import it.polimi.model.exceptions.MazzoVuotoException;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
    Mazzo(List<Carta> lista){
        this();
        Collections.shuffle(lista);
        this.carte.addAll(lista);
    }
    
    /**
     * Copy Constructor
     * @param source
     */
    public Mazzo(Mazzo source){
    	this(buildCopyOfCarte(source));
    }
    
    /**
     * di supporto per Copy Constructor
     * @param source
     * @return
     */
    private static List<Carta> buildCopyOfCarte(Mazzo source){
    	List<Carta> lista = new ArrayList<Carta>();
    	for(Carta carta : source.carte){
    		lista.add(new CartaSettore((CartaSettore) carta)); //ipotesi implicita: tutte le carte sono carte settore
    	}
    	return lista;
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
	
	@Override
	public int hashCode() {
		HashCodeBuilder hash =  new HashCodeBuilder(23, 17);
		for(Carta carta : this.carte)
				hash.append((CartaSettore) carta); // ipotesi tutte le carte sono carte settore
		return hash.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Mazzo other = (Mazzo) obj;
		if (this.carte.size() != other.carte.size())
			return false;
		EqualsBuilder equals = new EqualsBuilder();
		List<Carta> thisList = new ArrayList<Carta>(this.carte);
		List<Carta> otherList = new ArrayList<Carta>(other.carte);
		for(int i = 0; i < this.carte.size(); i++)
			equals.append((CartaSettore) thisList.get(i), (CartaSettore) otherList.get(i)); //ipotesi tutte le carte sono carte settore
		return equals.isEquals();
	}
}
