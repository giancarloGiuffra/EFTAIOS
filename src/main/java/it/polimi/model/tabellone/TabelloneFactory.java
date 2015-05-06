package it.polimi.model.tabellone;

import it.polimi.model.exceptions.*;

/**
 * Classe per creare il tabellone
 *
 */
public class TabelloneFactory {
    
    /**
     * Costruttore
     */
	private TabelloneFactory(){
    	//per nacondere il costruttore - suggerito da sonar
    }
	
	/**
     * Restituisce il tabellone indicato
     * @param tabellone nome del tabellone indicato
     * @return
     */
	public static Tabellone createTabellone(String tabellone){
        switch(tabellone){
            case "GALILEI": 
                return new TabelloneGalilei();
            default: 
                throw new IllegalTabelloneException(String.format(
                		"%s non è un tabellone valido",tabellone));
        }
    }
}
