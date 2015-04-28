package it.polimi.tabellone;

import it.polimi.model.sector.Settore;
import it.polimi.model.sector.TipoSettore;

import java.util.Map;

abstract class Tabellone {
    protected Map<String, Settore> tabellone;
    
    public Settore getSettore(String nome){
    	Settore.checkIfValidSectorName(nome);
    	if(!this.tabellone.containsKey(nome)) return new Settore(nome,TipoSettore.PERICOLOSO);
    	return this.tabellone.get(nome);
    }
}
