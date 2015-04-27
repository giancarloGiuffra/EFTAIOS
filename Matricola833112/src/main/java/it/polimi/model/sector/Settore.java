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
    public String getNome(){ //restituisce "A01" se col="A" e riga=1
        return new StringBuilder().append(col).append(String.format("%02d", this.riga)).toString();
    }
    
    public boolean isValidSectorName(char col, int riga){
        return (1 <= riga && riga <= ULTIMA_RIGA ) &&
               ('A' <= Character.toUpperCase(col) && Character.toUpperCase(col) <= ULTIMA_COLONNA);
    }
    
    private Settore(String nome, TipoSettore tipo){
    	this(getColonnaFromName(nome), getRigaFromName(nome), tipo);
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
    
    /*private class SectorPosition{
        private final char col;
        private final int riga;
        
        public SectorPosition(char col, int riga){this.col=col; this.riga=riga;}
        public char getCol(){return this.col;}
        public int getRiga(){return this.riga;}
    }
    
    public SectorPosition getSectorPositionFromName(String s){
        final String REGEX = "[A-W](0[1-9]|1[0-4])";
        if (!s.matches(REGEX)) throw new BadSectorPositionNameException(String.format("%s non è un nome di settore valido", s));
        char colonna = s.charAt(0);
        int riga = Integer.parseInt(s.substring(1, 2));
        return new SectorPosition(colonna,riga);
    }*/
    
    private static void checkIfValidSectorName(String nome){
    	final String REGEX = "[A-W](0[1-9]|1[0-4])";
    	if( !nome.matches(REGEX)) throw new BadSectorPositionNameException(String.format("%s non è un nome di settore valido", nome));
    }
    
    private static char getColonnaFromName(String nome){
    	checkIfValidSectorName(nome);
        return nome.charAt(0);
    }
    
    private static int getRigaFromName(String nome){
    	checkIfValidSectorName(nome);
        return Integer.parseInt(nome.substring(1, 2));
    }
    
    public List<Settore> getListSettoriDiTipo(List<String> nomiSettori, TipoSettore tipo){
        List<Settore> listaSettori = new ArrayList<Settore>();
        for(String s : nomiSettori){
            /*SectorPosition sectorPosition = this.getSectorPositionFromName(s);
            listaSettori.add(new Settore(sectorPosition.getCol(),sectorPosition.getRiga(),tipo));
        	*/
        	listaSettori.add(new Settore(s,tipo));
        } 
        return listaSettori;
    }
    
    
}
