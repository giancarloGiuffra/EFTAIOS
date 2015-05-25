package it.polimi.common.observer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Classe base per Observable
 *
 */
public abstract class BaseObservable {
	
	private final List<BaseObserver> observers;
	
	/**
	 * Costruttore
	 */
	public BaseObservable(){
		this.observers = new ArrayList<BaseObserver>();
	}
	
	public void addObserver(BaseObserver obs){
		this.observers.add(obs);
	}
	
	public void deleteObserver(BaseObserver obs){
		this.observers.remove(obs);
	}
	
	/**
	 * Notifica l'evento a tutti gli Observers registrati
	 * @param event
	 */
	protected final void notify(Event event){
		for(BaseObserver obs:this.observers){
			obs.notifyRicevuto(this, event);
		}
	}

}
