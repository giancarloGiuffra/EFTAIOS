package it.polimi.common.observer;

public final class ModelDichiaratoSilenzioEvent extends Event {

    /**
     * Costruttore
     * @param msg
     */
    public ModelDichiaratoSilenzioEvent() {
        super("Il giocatore ha dichiarato silenzio");
    }

}
