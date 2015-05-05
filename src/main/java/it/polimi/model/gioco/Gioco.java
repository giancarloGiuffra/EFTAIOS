package it.polimi.model.gioco;

import it.polimi.model.carta.Mazzo;
import it.polimi.model.player.Player;
import it.polimi.model.player.PlayerFactory;
import it.polimi.model.player.Razza;
import it.polimi.model.sector.Settore;
import it.polimi.model.tabellone.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Queue;

public class Gioco extends Observable {

    private final Tabellone tabellone;
    private Mazzo mazzoDiCarteSettore;
    private Map<Player,Settore> positions;
    private Queue<Player> players; //Per gestire i turni
    
    /**
     * Costruttore
     * @param numGiocatori numero di giocatori
     */
    public Gioco(int numGiocatori) {
        this.tabellone = TabelloneFactory.createTabellone("GALILEI");
        this.mazzoDiCarteSettore = Mazzo.creaNuovoMazzoCarteSettore();
        List<Player> players = PlayerFactory.createPlayers(numGiocatori);
        Collections.shuffle(players); //primo giocatore random
        this.players = new LinkedList<Player>(players);
        for(Player player:this.players){
            if(player.razza()==Razza.HUMAN){
                positions.put(player, tabellone.baseUmana());
            }else{
                positions.put(player, tabellone.baseAliena());
            }
        }
    }
    
    /**
     * @return	il giocatore a cui tocca giocare
     */
    public Player nextPlayer(){
    	return this.players.element();
    }
    
    /**
     * Passa il giocatore del turno corrente alla fine
     */
    public void finishTurn(){
    	this.players.add(this.players.remove());
    }
    
    /**
     * Gestisce un turno
     */
    public void manageRound(){
    	//TODO
    }

}
