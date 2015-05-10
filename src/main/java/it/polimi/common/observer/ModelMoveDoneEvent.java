package it.polimi.common.observer;

public final class ModelMoveDoneEvent extends Event {
    
    private final String SETTORE_DESTINO;
    
    /**
     * Costruttore
     * @param to string col nome del settore
     */
    public ModelMoveDoneEvent(String to){
        super(buildMsg(to));
        this.SETTORE_DESTINO = to;
    }
    
    /**
     * Genera messaggio che descrive l'evento
     * @param to
     * @return
     */
    private static String buildMsg(String to){
        return new StringBuilder().append("Giocatore spostato a ").append(to).toString();
    }
    
    /**
     * @return il nome del settore di destinazione
     */
    public String settoreDestinazione(){
        return this.SETTORE_DESTINO;
    }
}
