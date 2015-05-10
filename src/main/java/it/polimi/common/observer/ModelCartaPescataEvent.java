package it.polimi.common.observer;

import it.polimi.model.carta.Carta;

public final class ModelCartaPescataEvent extends Event {

    private final Carta carta;
    
    /**
     * Costruttore
     * @param carta
     */
    public ModelCartaPescataEvent(Carta carta) {
        super(String.format("Pescata carta %s", carta.nome()));
        this.carta = carta;
    }
    
    /**
     * @return carta associata all'evento
     */
    public Carta carta(){
        return this.carta;
    }

}
