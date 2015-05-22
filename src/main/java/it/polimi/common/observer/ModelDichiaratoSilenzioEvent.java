package it.polimi.common.observer;

public final class ModelDichiaratoSilenzioEvent extends Event {

    private final String player;
	
	/**
     * Costruttore
     * @param msg
     */
    public ModelDichiaratoSilenzioEvent(String playerNome) {
        super("Il giocatore ha dichiarato silenzio");
        this.player = playerNome;
    }
    
    /**
     * 
     * @return nome del player che dichiara silenzio
     */
    public String player(){
    	return this.player;
    }

}
