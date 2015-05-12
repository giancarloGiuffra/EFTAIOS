package it.polimi.common.observer;

import java.util.ArrayList;
import java.util.List;

import it.polimi.model.player.Player;
import it.polimi.model.sector.Settore;

public final class ModelAttaccoEvent extends Event {

    private final Player player;
    private final Settore settore;
    private final List<Player> morti = new ArrayList<Player>();
    
    /**
     * Costruttore
     * @param player
     * @param settore
     * @param morti
     */
    public ModelAttaccoEvent(Player player, Settore settore, List<Player> morti) {
        super(buildMsg(player, settore, morti));
        this.player = player;
        this.settore = settore;
        this.morti.addAll(morti);
    }

    /**
     * Crea messaggio che descrive l'evento
     * @param player
     * @param settore
     * @param morti
     * @return
     */
    private static String buildMsg(Player player, Settore settore,
            List<Player> morti) {
        StringBuilder nomiMorti = new StringBuilder();
        for(Player playerMorto : morti){
            nomiMorti.append(playerMorto.nome()).append("\n");
        }
        
        StringBuilder msg =  new StringBuilder();
        msg.
        append("Attacco in settore ").
        append(settore.getNome()).
        append(" effettuato da ").
        append(player.nome()).append("\n");
        
        if(nomiMorti.toString().isEmpty()){
            return msg.append("Non ci sono stati morti").toString();
        } else {
            return msg.
                    append("I morti sono stati i seguenti:\n").
                    append(nomiMorti.toString()).toString();
        }
    }
    
    /**
     * 
     * @return player che ha effettuato l'attacco
     */
    public Player player(){
        return this.player;
    }
    
    /**
     * 
     * @return settore dove è stato effettuato l'attacco
     */
    public Settore settore(){
        return this.settore;
    }
    
    /**
     * 
     * @return lista di morti dopo l'attacco
     */
    public List<Player> morti(){
        return this.morti;
    }

}
