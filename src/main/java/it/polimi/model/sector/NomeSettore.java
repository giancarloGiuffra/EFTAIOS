package it.polimi.model.sector;

import it.polimi.model.exceptions.BadSectorPositionNameException;

/**
 * Classe per rappresentare il nome di un Settore
 * Nomi ammissibili sono A01 fino a W14
 * @deprecated
 */
@Deprecated
public class NomeSettore {
    
    static final String REGEX = "[A-W](0[1-9]|1[0-4])";
    private String nome;
    private char col;
    private int riga;
    
    /**
     * Costruttore
     * @param nome nome del settore
     */
    NomeSettore(String nome){
        if( !nome.matches(REGEX)) throw new BadSectorPositionNameException(String.format("%s non è un nome di settore valido", nome));
        this.nome = nome;
        this.col = nome.charAt(0);
        this.riga = Integer.parseInt(nome.substring(1, 2));
    }
    
    /**
     * @return colonna del settore
     */
    public char getCol(){
        return this.col;
    }
    
    /**
     * @return riga del settore
     */
    public int getRiga(){
        return this.riga;
    }
    
    /**
     * @return nome sel settore
     */
    public String getNome(){
        return this.nome;
    }
}
