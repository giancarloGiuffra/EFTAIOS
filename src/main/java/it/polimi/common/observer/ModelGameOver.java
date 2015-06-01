package it.polimi.common.observer;

import it.polimi.model.exceptions.PlayerNonSignificativoPerQuestoGameOver;
import it.polimi.model.gioco.TipoGameOver;
import it.polimi.model.player.Player;
import it.polimi.model.player.PlayerFactory;

public final class ModelGameOver extends Event {

    private final TipoGameOver tipo;
    private Player player = PlayerFactory.createPlayer();
    
    /**
     * Costruttore
     * @param player
     */
    public ModelGameOver(Player player) {
        super(buildMsg(player));
        this.tipo = TipoGameOver.UMANO_IN_SCIALUPPA;
        this.player = player;
    }
    
    /**
     * Costruttore
     * @param tipo
     */
    public ModelGameOver(TipoGameOver tipo){
        super(buildMsg(tipo));
        this.tipo = tipo;
    }

    /**
     * Costruisce messaggio che descrive l'evento a seconda del tipo
     * @param tipo
     * @return
     */
    private static String buildMsg(TipoGameOver tipo) {
        StringBuilder msg = new StringBuilder();
        switch(tipo){
            case UMANI_MORTI:
                msg.append("Tutti gli umani sono morti");
                break;
            case TURNI_FINITI:
                msg.append("I turni sono finiti");
                break;
            default:
                msg.append("Un umano è arrivato in scialuppa");
        }
        return msg.toString();
    }

    /**
     * Costruisce il messaggio che descrive l'evento in caso di umano in scialuppa
     * @param player
     * @return
     */
    private static String buildMsg(Player player) {
        return new StringBuilder().
                append(player.nome()).
                append(" è arrivato in scialuppa").
                toString();
    }
    
    /**
     * 
     * @return il tipo di Game Over
     */
    public TipoGameOver tipo(){
        return this.tipo;
    }
    
    /**
     * 
     * @return il giocatore nel caso di umano in scialuppa
     */
    public Player player(){
        if(this.tipo == TipoGameOver.UMANO_IN_SCIALUPPA) return this.player;
        throw new PlayerNonSignificativoPerQuestoGameOver("Il giocatore non è significativo per questo tipo di game over");
    }

}
