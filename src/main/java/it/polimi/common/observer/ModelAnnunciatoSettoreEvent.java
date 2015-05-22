package it.polimi.common.observer;

public final class ModelAnnunciatoSettoreEvent extends Event {

    private final String settore;
    private final String player;
    
    /**
     * Costruttore
     * @param msg
     */
    public ModelAnnunciatoSettoreEvent(String nomeSettore, String nomePlayer) {
        super(String.format("Il giocatore annuncia rumore nel settore %s", nomeSettore));
        this.player = nomePlayer;
        this.settore = nomeSettore;
    }
    
    /**
     * @return settore annunciato
     */
    public String settore(){
        return this.settore;
    }
    
    /**
     * @return player che fa l'annuncio
     */
    public String player(){
        return this.player;
    }

}
