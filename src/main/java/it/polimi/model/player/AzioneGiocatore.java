package it.polimi.model.player;

public enum AzioneGiocatore {
    ANNUNCIA_SETTORE("Annunciare un settore"),
    ANNUNCIA_SETTORE_MIO("Annunciare il tuo settore"),
    DICHIARA_SILENZIO("Dichiarare silenzio"),
    PESCA_CARTA("Pescare una carta"),
    ATTACCA("Attaccare"),
    NON_ATTACCA("Non Attaccare");
    
    private final String nome;
    
    /**
     * Costruttore
     * @param nome
     */
    private AzioneGiocatore(String nome){
    	this.nome = nome;
    }
    
    /**
     * restituisce nome dell'azione
     */
    public String nome(){
    	return this.nome;
    }
}