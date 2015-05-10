package it.polimi.model.tabellone;

import it.polimi.model.sector.Settore;
import it.polimi.model.sector.TipoSettore;

import java.util.List;
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

    /**
     * 
     * @return base umana
     */
    public Settore baseUmana() {
        return this.baseUmana;
    }

    /**
     * 
     * @return base aliena
     */
    public Settore baseAliena() {
        return this.baseAliena;
    }
    
    /**
     * Controlla se esiste un sentiero valido tra i due settori
     * nel caso di settori a distanza più di due return true
     * @param from
     * @param to
     * @return
     */
    public boolean esisteSentieroValido(Settore from, Settore to){
        if(from.isInaccessibile() || to.isInaccessibile()) return false;
        if(from.isOneSectorAway(to)) return true;
        if(from.isTwoSectorAway(to)){
            List<String> listaIntersezione = from.getSettoriAdiacenti();
            listaIntersezione.retainAll(to.getSettoriAdiacenti()); //adesso contiene l'intersezione
            for(String nome : listaIntersezione){
                if(!this.sectors.get(nome).isInaccessibile()) return true;
            }
            return false;
        }
        return true;
    }
}
