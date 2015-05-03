package it.polimi.model.gioco;

import it.polimi.model.carta.Mazzo;
import it.polimi.model.player.Player;
import it.polimi.model.player.PlayerFactory;
import it.polimi.model.player.Razza;
import it.polimi.model.sector.Settore;
import it.polimi.model.tabellone.*;

import java.util.List;
import java.util.Map;
import java.util.Observable;

public class Gioco extends Observable {

    private final Tabellone tabellone;
    private Mazzo mazzoDiCarteSettore;
    private Map<Settore,Player> positions;
    
    public Gioco(int numGiocatori) {
        this.tabellone = TabelloneFactory.createTabellone("GALILEI");
        this.mazzoDiCarteSettore = Mazzo.creaNuovoMazzoCarteSettore();
        List<Player> players = PlayerFactory.createPlayers(numGiocatori);
        for(Player player:players){
            if(player.razza()==Razza.HUMAN){
                positions.put(tabellone.baseUmana(), player);
            }else{
                positions.put(tabellone.baseAliena(), player);
            }
        }
    }

}
