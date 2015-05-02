package it.polimi.model.tabellone;

import it.polimi.model.sector.Settore;
import it.polimi.model.sector.TipoSettore;

import java.util.Map;

abstract class Tabellone {
    protected Map<String, Settore> sectors;
    
    public Settore getSettore(String nome){
    	Settore.checkIfValidSectorName(nome);
    	if(!this.sectors.containsKey(nome)) return new Settore(nome,TipoSettore.PERICOLOSO);
    	return this.sectors.get(nome);
    }
}
