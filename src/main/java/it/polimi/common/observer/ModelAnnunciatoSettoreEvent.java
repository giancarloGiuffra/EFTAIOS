package it.polimi.common.observer;

public final class ModelAnnunciatoSettoreEvent extends Event {

    private final String settore;
    
    /**
     * Costruttore
     * @param msg
     */
    public ModelAnnunciatoSettoreEvent(String nomeSettore) {
        super(String.format("Il giocatore annuncia rumore nel settore %s", nomeSettore));
        this.settore = nomeSettore;
    }
    
    /**
     * @return settore annunciato
     */
    public String settore(){
        return this.settore;
    }

}
