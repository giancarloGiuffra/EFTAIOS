package it.polimi.model.tabellone;

import it.polimi.model.exceptions.BadSectorException;
import it.polimi.model.sector.Settore;
import it.polimi.model.sector.TipoSettore;

import java.util.ArrayList;
import java.util.Arrays;
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
     * Controlla se esiste un sentiero valido tra i due settori (distanza tra di loro al massimo due)
     * NB: nel caso di settori a distanza più di due return true
     * @param from
     * @param to
     * @return
     */
    public boolean esisteSentieroValido(Settore from, Settore to){
        if(from.isInaccessibile() || to.isInaccessibile()) throw new BadSectorException(String.format("uno o entrambi settori %s, %s sono inacessibili ", to.getNome(),from.getNome()));
        if(from.isOneSectorAway(to)) return true;
        if(from.isTwoSectorAway(to)){
            List<String> listaIntersezione = from.getSettoriAdiacenti();
            listaIntersezione.retainAll(to.getSettoriAdiacenti()); //adesso contiene l'intersezione
            for(String nome : listaIntersezione){
                if(!this.getSettore(nome).isInaccessibile()) return true;
            }
            throw new BadSectorException(String.format("Non esiste un sentiero valido tra i settori %s e %s", from.getNome(),to.getNome()));
        }
        return true; //settori a più di due di distanza
    }
    
    /**
     * Restituisce la lista di settori del tipo indicato di questo tabellone
     * @param tipo
     * @return
     */
    public List<Settore> getSettoriDiTipo(TipoSettore tipo){
        List<Character> colonne = Arrays.asList('A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W');
        List<Integer> righe = Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14);
        List<Settore> lista = new ArrayList<Settore>();
        switch(tipo){
            case BASE_HUMAN: 
                lista.add(this.baseUmana);
                break;
            case BASE_ALIEN:
                lista.add(this.baseAliena);
                break;
            case INACCESSIBILE: case SICURO: case PERICOLOSO: case SCIALUPPA:
                for(Character colonna : colonne){
                    for(Integer riga : righe){
                        if(tipo == this.getSettore(Settore.buildNomeSettore( colonna, riga)).getTipo())
                            lista.add(this.getSettore(Settore.buildNomeSettore( colonna, riga)));
                    }
                }
                break;
            default:
                break;
        }
        return lista;
    }
}
