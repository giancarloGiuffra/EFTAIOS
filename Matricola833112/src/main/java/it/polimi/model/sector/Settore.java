package it.polimi.model.sector;

import it.polimi.model.exceptions.BadSectorException;
import it.polimi.model.exceptions.BadSectorPositionNameException;

import java.util.ArrayList;
import java.util.List;

public class Settore {
    
    private final char col;
    private final int riga;
    private final TipoSettore tipo;
    
    private static final char ULTIMA_COLONNA = 'W';
    private static final  int ULTIMA_RIGA = 14;
    
    Settore(char col, int riga, TipoSettore tipo){
        if (this.isValidSectorName(col, riga)){
            this.col=Character.toUpperCase(col); this.riga=riga; this.tipo=tipo;
        } else{
            throw new BadSectorException(String.format("Colonna %c e/o Riga %d non valida(e)", col, riga));
        }        
    }
    
    public char getColonna(){return this.col;}
    public int getRiga(){return this.riga;}
    public TipoSettore getTipo(){return this.tipo;}
    public String getNome(){ //returns "A01" if col="A" and riga=1
        return new StringBuilder().append(col).append(String.format("%02d", this.riga)).toString();
    }
    
    public boolean isValidSectorName(char col, int riga){
        return (1 <= riga && riga <= ULTIMA_RIGA ) &&
               ('A' <= Character.toUpperCase(col) && Character.toUpperCase(col) <= ULTIMA_COLONNA);
    }
    
    public boolean isInaccessibile(){return this.tipo==TipoSettore.INACCESSIBILE;}
    public boolean isSicuro(){return this.tipo==TipoSettore.SICURO;}
    public boolean isPericoloso(){return this.tipo==TipoSettore.PERICOLOSO;}
    public boolean isScialuppa(){return this.tipo==TipoSettore.SCIALUPPA;}
    public boolean isBaseUmana(){return this.tipo==TipoSettore.BASE_HUMAN;}
    public boolean isBaseALiena(){return this.tipo==TipoSettore.BASE_ALIEN;}
    
    public boolean isBase(){return this.isBaseUmana() || this.isBaseALiena();}
    
    public boolean isValidDestinationForHuman(){return  this.isSicuro() ||
                                                        this.isPericoloso() ||
                                                        this.isScialuppa();}
    
    public boolean isValidDestinationForAlien(){return  this.isSicuro() || 
                                                        this.isPericoloso();}
    
    public boolean isOneSectorAway(Settore settore){
        //TODO
        return false;
    }
    public boolean isTwoSectorAway(Settore settore){
        //TODO
        return false;
    }
    
    static public List<Settore> getListSettoriDiTipo(List<String> nomiSettori, TipoSettore tipo){
        List<Settore> listaSettori = new ArrayList<Settore>();
        for(String s : nomiSettori){
            NomeSettore nome = new NomeSettore(s);
        	listaSettori.add(new Settore(nome.getCol(), nome.getRiga(),tipo));
        } 
        return listaSettori;
    }
    
    
}
