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
        if (!this.isValidSectorName(col, riga)) throw new BadSectorException(String.format("Colonna %c e/o Riga %d non valida(e)", col, riga));
        this.col=Character.toUpperCase(col); this.riga=riga; this.tipo=tipo;     
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
    
    public Settore(String nome, TipoSettore tipo){
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
    	return (Math.abs(this.distanceColonna(settore))==1 && (this.distanceRiga(settore)==-1 || this.distanceRiga(settore)==0 ) ) ||
    		   (this.distanceColonna(settore)==0 && Math.abs(this.distanceRiga(settore))==1);
    }
    
    private int distanceRiga(Settore settore) {
		return this.riga-settore.riga;
	}

	private int distanceColonna(Settore settore) {
		return this.col-settore.col;
	}

	public boolean isAtMostTwoSectorAway(Settore settore){
        return isOneSectorAway(settore) ||
        	   isTwoSectorAway(settore);
    }
    
    private boolean isTwoSectorAway(Settore settore) {
		return ( this.distanceColonna(settore)==0 && Math.abs(this.distanceRiga(settore))==2 ) ||
			   ( Math.abs(this.distanceColonna(settore))==1 && ( this.distanceRiga(settore)==-2 || this.distanceRiga(settore)==1 ) ) ||
			   ( Math.abs(this.distanceColonna(settore))==2 && Math.abs(this.distanceRiga(settore))<=1 );
	}

	public static void checkIfValidSectorName(String nome){
    	final String REGEX = "[A-W](0[1-9]|1[0-4])";
    	if( !nome.matches(REGEX)) throw new BadSectorPositionNameException(String.format("%s non è un nome di settore valido", nome));
    }
    
    public static char getColonnaFromName(String nome){
    	checkIfValidSectorName(nome);
        return nome.charAt(0);
    }
    
    public static int getRigaFromName(String nome){
    	checkIfValidSectorName(nome);
        return Integer.parseInt(nome.substring(1, 2));
    }
    
    public static List<Settore> getListSettoriDiTipo(List<String> nomiSettori, TipoSettore tipo){
        List<Settore> listaSettori = new ArrayList<Settore>();
        for(String s : nomiSettori){
        	listaSettori.add(new Settore(s,tipo));
        } 
        return listaSettori;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + col;
		result = prime * result + riga;
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Settore other = (Settore) obj;
		if (col != other.col)
			return false;
		if (riga != other.riga)
			return false;
		if (tipo != other.tipo)
			return false;
		return true;
	}
    
    
}
