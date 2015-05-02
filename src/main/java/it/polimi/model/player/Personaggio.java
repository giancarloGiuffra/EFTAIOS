package it.polimi.model.player;

/**
 * Enumeration type per i Personaggi del gioco.
 * L'ordine nel quale sono stati disposti è importante perchè permette
 * di gestire in modo semplice il requisito di gioco di avere un alieno in più
 * rispetto al numero di umani nel caso di numero di giocatori dispari.
 *
 */
public enum Personaggio {
    ALIENO1(Razza.ALIEN,"Piero Ceccarella"),
    CAPITANO(Razza.HUMAN,"Ennio Maria Dominoni"),
    ALIENO2(Razza.ALIEN,"Vittorio Martana"),
    PILOTA(Razza.HUMAN,"Julia Niguloti"),
    ALIENO3(Razza.ALIEN,"Maria Galbani"),
    PSICOLOGO(Razza.HUMAN,"Silvano Porpora"),
    ALIENO4(Razza.ALIEN,"Paolo Landon"),
    SOLDATO(Razza.HUMAN,"Tuccio Brendon");
     
    private final Razza razza;
    private final String nome;
    private static final int NRO_PERSONAGGI = 8;
    
    /**
     * Costruttore
     * @param razza
     * @param nome
     */
    private Personaggio(Razza razza, String nome){
        this.razza = razza;
        this.nome = nome;
    }
    
    /**
     * @return razza del Personaggio
     */
    public Razza razza(){
        return this.razza;
    }
    
    /**
     * @return nome del Personaggio
     */
    public String nome(){
        return this.nome;
    }
    
    /**
     * @return numero totale di personaggi
     */
    public static int numeroPersonaggi(){
        return Personaggio.NRO_PERSONAGGI;
    }
    
    /**
     * @return true se il Personaggio è umano, false altrimenti.
     */
    public boolean isHuman(){
        return this.razza==Razza.HUMAN;
    }
    
    /**
     * @return true se il Personaggio è alieno, false altrimenti.
     */
    public boolean isAlien(){
        return this.razza==Razza.ALIEN;
    }
}
