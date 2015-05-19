package it.polimi.model.sector;

import it.polimi.model.exceptions.BadSectorException;
import it.polimi.model.exceptions.BadSectorPositionNameException;
import it.polimi.model.player.Player;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Classe per rappresentare i settori.
 *
 */
public class Settore {
    
    private final Character col;
    private final Integer riga;
    private final TipoSettore tipo;
    
    private static final char ULTIMA_COLONNA = 'W';
    private static final  int ULTIMA_RIGA = 14;
    
    /**
     * Costruttore
     * @param col colonna del settore
     * @param riga riga del settore
     * @param tipo tipo di settore
     */
    private Settore(char col, int riga, TipoSettore tipo){
        if (!this.isValidSectorName(col, riga)) throw new BadSectorException(String.format("Colonna %c e/o Riga %d non valida(e)", col, riga));
        this.col=Character.toUpperCase(col);
        this.riga=riga;
        this.tipo=tipo;     
    }
    
    /**
     * Costruttore
     * @param nome nome del settore, formato a 3 caratteri, eg: A01, W14
     * @param tipo tipo del settore
     */
    public Settore(String nome, TipoSettore tipo){
        this(getColonnaFromName(nome), getRigaFromName(nome), tipo);
    }
    
    /**
     * @return colonna del settore
     */
    public Character getColonna(){
        return this.col;
    }
    
    /**
     * @return riga del settore
     */
    public Integer getRiga(){
        return this.riga;
    }
    
    /**
     * @return tipo del settore
     */
    public TipoSettore getTipo(){
        return this.tipo;
    }
    
    /**
     * Nome del settore, formato a tre caratteri, eg: A01, W14.
     * @return nome del settore
     */
    public String getNome(){
        return new StringBuilder().append(col).append(String.format("%02d", this.riga)).toString();
    }
    
    /**
     * Verifica se la colonna e la riga corrispondono a un settore valido
     * @param col colonna del supposto settore
     * @param riga riga del supposto settore
     * @return true se il settore è valido, false altrimenti
     */
    public boolean isValidSectorName(char col, int riga){
        return (1 <= riga && riga <= ULTIMA_RIGA ) &&
               ('A' <= Character.toUpperCase(col) && Character.toUpperCase(col) <= ULTIMA_COLONNA);
    }
    
    /**
     * Verifica se il settore può essere annunciato
     * @return
     */
    public boolean isValidSectorForAnnouncement(){
        return this.isPericoloso() || this.isSicuro();
    }
    
    /**
     * Verifica se il settore è inaccessibile
     * @return true se il settore è inaccessibile, false altrimenti
     */
    public boolean isInaccessibile(){
        return this.tipo==TipoSettore.INACCESSIBILE;
    }
    
    /**
     * Verifica se il settore è sicuro
     * @return true se il settore è sicuro, false altrimenti
     */
    public boolean isSicuro(){
        return this.tipo==TipoSettore.SICURO;
    }
    
    /**
     * Verifica se il settore è pericoloso
     * @return true se il settore è pericoloso, false altrimenti
     */
    public boolean isPericoloso(){
        return this.tipo==TipoSettore.PERICOLOSO;
    }
    
    /**
     * Verifica se il settore è una scialuppa
     * @return true se il settore è scialuppa, false altrimenti
     */
    public boolean isScialuppa(){
        return this.tipo==TipoSettore.SCIALUPPA;
    }
    
    /**
     * Verifica se il settore è la base umana
     * @return true se il settore è la base umana, false altrimenti
     */
    public boolean isBaseUmana(){
        return this.tipo==TipoSettore.BASE_HUMAN;
    }
    
    /**
     * Verifica se il settore è la base aliena
     * @return true se il settore è la base aliena, false altrimenti
     */
    public boolean isBaseALiena(){
        return this.tipo==TipoSettore.BASE_ALIEN;
    }
    
    /**
     * Verifica se il settore è una base
     * @return true se il settore è una base, false altrimenti
     */
    public boolean isBase(){
        return this.isBaseUmana() || this.isBaseALiena();
    }
    
    /**
     * Verifica se il settore è una destinazione valida per un giocatore umano
     * @return true se il settore è una destinazione valida per un giocatore umano, false altrimenti
     */
    public boolean isValidDestinationForHuman(){
        if(!this.isSicuro() &&
           !this.isPericoloso() &&
           !this.isScialuppa()) throw new BadSectorException(String.format("Il settore %s no è una destinazione valida per umani, è di tipo %s", this.getNome(),this.getTipo().toString()));
        return true;
    }
    
    /**
     * Verifica se il settore è una destinazione valida per un giocatore alieno
     * @return true se il settore è una destinazione valida per un giocatore alieno, false altrimenti
     */
    public boolean isValidDestinationForAlien(){
        if(!this.isPericoloso() &&
           !this.isSicuro()) throw new BadSectorException(String.format("Il settore %s no è una destinazione valida per alieni, è di tipo %s", this.getNome(),this.getTipo().toString()));
        return true;
    }
    
    /**
     * Verifica se il settore è a un settore di distanza
     * @param settore settore in input
     * @return true se il settore è a un settore di distanza
     */
    public boolean isOneSectorAway(Settore settore){
    	return isLeftOrRight(settore) ||
    	        isUpOrDown(settore);
    }
    
    /**
     * Calcola la distanza tra le righe di questo settore e quello dato in input
     * @param settore settore in input
     * @return distanza tra le righe di questo settore e quello in input
     */
    private int distanceRiga(Settore settore) {
		return this.riga-settore.riga;
	}

	/**
	 * Calcola la distanza tra le colonne di questo settore e quello dato in input
     * @param settore settore in input
     * @return distanza tra le colonne di questo settore e quello in input
     */
    private int distanceColonna(Settore settore) {
		return this.col-settore.col;
	}
    
    /**
     * Verifica se il settore è uno dei settori a destra oppure a sinistra
     * @param settore
     * @return true se il settore è uno dei due a sinistra oppure uno dei due a destra
     */
    private boolean isLeftOrRight(Settore settore){
        if(this.hasEvenColumn()){
            return Math.abs(this.distanceColonna(settore))==1 && 
                    (this.distanceRiga(settore)==-1 || this.distanceRiga(settore)==0 );
        } else { //this.hasOddColumn()
            return Math.abs(this.distanceColonna(settore))==1 && 
                    (this.distanceRiga(settore)==1 || this.distanceRiga(settore)==0 );
        }
    }

    /**
     * Verifica se la colonna è "pari", A è dispari, B è pari
     * @return
     */
    private boolean hasEvenColumn() {
        return (this.col-'A'+1)%2 == 0;
    }

    /**
     * Verifica se il settore è sopra oppure sotto
     * @param settore
     * @return true se il settore è il settore di sopra oppure quello di sotto
     */
    private boolean isUpOrDown(Settore settore){
        return this.distanceColonna(settore)==0 && Math.abs(this.distanceRiga(settore))==1;
    }
    
    /**
     * Verifica se il settore è al massimo a due settori di distanza
     * se il settore in input è lui stesso restituisce false
     * @param settore settore in input
     * @return true se il settore è al massimo a due settori di distanza
     */
    public boolean isAtMostTwoSectorAway(Settore settore){
        return isOneSectorAway(settore) ||
        	   isTwoSectorAway(settore);
    }
    
    /**
     * Verifica se il settore è a due settori di distanza
     * @param settore settore in input
     * @return true se il settore è a due settori di distanza
     */
    public boolean isTwoSectorAway(Settore settore) {
		return isUpOrDownTwo(settore) ||
		       isLeftOrRightTwo(settore) ||
		       isTwoSectorAwayInAdjacentColumns(settore);
	}
    
    /**
     * Verifica se il settore è sopra oppure sotto di due spazi
     * @param settore
     * @return true se soddisfa la condizione
     */
    private boolean isUpOrDownTwo(Settore settore){
        return this.distanceColonna(settore)==0 && Math.abs(this.distanceRiga(settore))==2;
    }
    
    /**
     * Verifica se il settore è a distanza due nelle colonne adiacenti
     * @param settore
     * @return true se soddisfa la condizione
     */
    private boolean isTwoSectorAwayInAdjacentColumns(Settore settore){
        if( this.hasEvenColumn() ){
            return Math.abs(this.distanceColonna(settore))==1 && 
                    ( this.distanceRiga(settore)==-2 || this.distanceRiga(settore)==1);
        } else { //this.hasOddColumn()
            return Math.abs(this.distanceColonna(settore))==1 && 
                    ( this.distanceRiga(settore)==2 || this.distanceRiga(settore)==-1);
        }
    }
    
    /**
     * Verifica se il settore è a destra oppure a sinistra di due spazi
     * @param settore
     * @return true se soddisfa la condizione
     */
    private boolean isLeftOrRightTwo(Settore settore){
        return Math.abs(this.distanceColonna(settore))==2 && Math.abs(this.distanceRiga(settore))<=1;
    }

	/**
	 * Verifica se il nome del settore è valido
	 * @param nome nome del settore da verificare
	 * @thow BadSectorPositionNameException
	 */
    public static void checkIfValidSectorName(String nome){
    	final String regex = "[a-wA-W](0[1-9]|1[0-4])";
    	if( !nome.matches(regex)) throw new BadSectorPositionNameException(String.format("%s non è un nome di settore valido", nome));
    }
    
    /**
     * @param nome del settore
     * @return colonna del settore
     */
    public static char getColonnaFromName(String nome){
    	checkIfValidSectorName(nome);
        return nome.charAt(0);
    }
    
    /**
     * @param nome del settore
     * @return riga del settore
     */
    public static int getRigaFromName(String nome){
    	checkIfValidSectorName(nome);
        return Integer.parseInt(nome.substring(1));
    }
    
    /**
     * Restituisce una lista di settori del tipo indicato 
     * @param nomiSettori lista di nomi di settori, formato a tre caratteri, eg: A01, W14
     * @param tipo dei settori
     * @return lista di settori cui nomi sono stati indicati in input
     */
    public static List<Settore> getListSettoriDiTipo(List<String> nomiSettori, TipoSettore tipo){
        List<Settore> listaSettori = new ArrayList<Settore>();
        for(String s : nomiSettori){
        	listaSettori.add(new Settore(s,tipo));
        } 
        return listaSettori;
    }
    
    /**
     * 
     * @return lista con i nomi dei settori adiacenti
     */
    public List<String> getSettoriAdiacenti(){
        List<String> vicini = new ArrayList<String>();
        if(!this.isPrimaRiga()) vicini.add(buildNomeSettore(this.col,this.riga-1));
        if(!this.isUltimaRiga()) vicini.add(buildNomeSettore(this.col,this.riga+1));
        if(!this.isPrimaColonna()) vicini.add(buildNomeSettore(this.col-1,this.riga));
        if(!this.isUltimaColonna()) vicini.add(buildNomeSettore(this.col+1,this.riga));
        if(this.hasEvenColumn()){
            if(!this.isPrimaColonna() && !this.isPrimaRiga()) vicini.add(buildNomeSettore(this.col-1,this.riga+1));
            if(!this.isUltimaColonna() && !this.isPrimaRiga()) vicini.add(buildNomeSettore(this.col+1,this.riga+1));
        } else{
            if(!this.isPrimaColonna() && !this.isUltimaRiga()) vicini.add(buildNomeSettore(this.col-1,this.riga-1));
            if(!this.isUltimaColonna() && !this.isUltimaRiga()) vicini.add(buildNomeSettore(this.col+1,this.riga-1));
        }
        return vicini;
    }
    
    /**
     * 
     * @return true se appartiene all'ultima colonna
     */
    private boolean isUltimaColonna() {
        return this.col == ULTIMA_COLONNA;
    }

    /**
     * 
     * @return true se appartiene alla prima colonna
     */
    private boolean isPrimaColonna() {
        return this.col == 'A';
    }

    /**
     * 
     * @return true se il settore appartiene all'ultima riga
     */
    private boolean isUltimaRiga() {
        return this.riga == ULTIMA_RIGA;
    }

    /**
     * 
     * @return true se il settore appartiene alla prima riga
     */
    private boolean isPrimaRiga() {
        return this.riga == 1;
    }

    /**
     * costruisce il nome del settore corrispondente
     * @param col
     * @param riga
     * @return
     */
    public static String buildNomeSettore(char col, int riga){
        return new StringBuilder().
                append( Character.toString(col).toUpperCase()).
                append(String.format("%02d", riga)).
                toString();
    }
    
    private static String buildNomeSettore(int col, int riga){
        return buildNomeSettore((char) col, riga);
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(11, 17).
                append(this.col).
                append(this.riga).
                toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Settore))
            return false;
        Settore other = (Settore) obj;
        return new EqualsBuilder().
                append(this.col,other.col).
                append(this.riga,other.riga).
                isEquals();
    }
}