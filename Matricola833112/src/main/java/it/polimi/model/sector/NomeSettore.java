package it.polimi.model.sector;

import it.polimi.model.exceptions.BadSectorPositionNameException;

public class NomeSettore {
    
    static final String REGEX = "[A-W](0[1-9]|1[0-4])";
    private String nome;
    private char col;
    private int riga;
    
    NomeSettore(String nome){
        if( !nome.matches(REGEX)) throw new BadSectorPositionNameException(String.format("%s non è un nome di settore valido", nome));
        this.nome = nome;
        this.col = nome.charAt(0);
        this.riga = Integer.parseInt(nome.substring(1, 2));
    }
    
    public char getCol(){return this.col;}
    public int getRiga(){return this.riga;}
    public String getNome(){return this.nome;}
}
