package it.polimi.model.tabellone;

import it.polimi.model.sector.Settore;
import it.polimi.model.sector.TipoSettore;

import java.util.Map;

/**
 * Classe per rappresentare il tabellone
 * Si opta per non salvare i settori pericolosi,
 * vengono salvati tutti gli altri e per default il resto sono i settori pericolosi.
 *
 */
public abstract class Tabellone {
    protected Map<String, Settore> sectors;
    protected Settore baseUmana;
    protected Settore baseAliena;
    
    /**
     * @param nome del settore
     * @return settore del nome indicato
     */
    public Settore getSettore(String nome){
    	Settore.checkIfValidSectorName(nome);
    	if(!this.sectors.containsKey(nome)) return new Settore(nome,TipoSettore.PERICOLOSO);
    	return this.sectors.get(nome);
    }

    public Settore baseUmana() {
        return this.baseUmana;
    }

    public Settore baseAliena() {
        return this.baseAliena;
    }
}
