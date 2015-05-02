package it.polimi.model.carta;

import it.polimi.model.exceptions.MazzoVuotoException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Mazzo {
	
	private Stack<Carta> carte;
	
	public Carta getCarta(){
		if(this.isEmpty()) throw new MazzoVuotoException("Il Mazzo è vuoto");
		return carte.pop();
	}
	
	private boolean isEmpty(){
		return carte.empty();
	}
	
	private Mazzo(List<Carta> lista){
	    Collections.shuffle(lista);
	    this.carte.addAll(lista);
	}
	
	private Mazzo creaNuovoMazzoCarteSettore(DistribuzioneCarteSettore distribuzione){
	    List<Carta> listaRumoreMio = CartaSettore.getListCarteSettoreDiTipo(TipoCartaSettore.RUMORE_MIO, distribuzione.rumoreMio());
	    List<Carta> listaRumoreQualunque = CartaSettore.getListCarteSettoreDiTipo(TipoCartaSettore.RUMORE_QUALUNQUE, distribuzione.rumoreQualunque());
	    List<Carta> listaSilenzio = CartaSettore.getListCarteSettoreDiTipo(TipoCartaSettore.SILENZIO, distribuzione.silenzio());
	    
	    List<Carta> lista = new ArrayList<Carta>();
	    lista.addAll(listaRumoreMio);
	    lista.addAll(listaRumoreQualunque);
	    lista.addAll(listaSilenzio);
	   
	    Mazzo mazzo = new Mazzo(lista);
	    return mazzo;	    
	}
	
	public Mazzo creaNuovoMazzoCarteSettore(){
	    return creaNuovoMazzoCarteSettore(DistribuzioneCarteSettore.EQUIDISTRIBUITA);
	}
	
	public void rimischia(List<Carta> listaCarte){
	    Collections.shuffle(listaCarte);
        this.carte.addAll(listaCarte);
	}
}
