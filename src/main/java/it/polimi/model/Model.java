package it.polimi.model;

import it.polimi.model.gioco.Gioco;

public class Model extends Gioco {

	/**
	 * Costruttore
	 * @param numGiocatori
	 */
    public Model(int numGiocatori) {
		super(numGiocatori);
	}
    
    /**
     * Copy Constructor
     * @param source
     */
    public Model(Model source){
    	super(source);
    }

}
