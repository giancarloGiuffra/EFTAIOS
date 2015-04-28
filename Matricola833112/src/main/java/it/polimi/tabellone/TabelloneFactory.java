package it.polimi.tabellone;

import it.polimi.model.exceptions.*;



public class TabelloneFactory {
	public Tabellone createTabellone(String tabellone){
        switch(tabellone){
            case "GALILEI": 
                return new TabelloneGalilei();
            default: 
                throw new IllegalTabelloneException(String.format(
                		"%s non è un tabellone valido",tabellone));
        }
    }
}
