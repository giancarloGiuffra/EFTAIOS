package it.polimi.model.sector;

public class Settore {
    
    private final char col;
    private final int riga;
    private final TipoSettore tipo;
    
    Settore(char col, int riga, TipoSettore tipo){
        this.col=col; this.riga=riga; this.tipo=tipo;
    }
    
    public char getColonna(){return this.col;}
    public int getRiga(){return this.riga;}
    public TipoSettore getTipo(){return this.tipo;}
    
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
    
    
}
