package it.polimi.model.gioco;

import it.polimi.model.carta.Mazzo;
import it.polimi.model.exceptions.IllegalMoveException;
import it.polimi.model.player.Player;
import it.polimi.model.player.PlayerFactory;
import it.polimi.model.player.Razza;
import it.polimi.model.sector.Settore;
import it.polimi.model.tabellone.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class Gioco {

	private final Tabellone tabellone;
    private Mazzo mazzoDiCarteSettore;
    private Map<Player,Settore> positions;
    private Turno turni; //Per gestire i turni
    
    /**
     * Costruttore
     * @param numGiocatori numero di giocatori
     */
    public Gioco(int numGiocatori) {
        this.tabellone = TabelloneFactory.createTabellone("GALILEI");
        this.mazzoDiCarteSettore = Mazzo.creaNuovoMazzoCarteSettore();
        this.turni = new Turno(PlayerFactory.createPlayers(numGiocatori));
        for(Player player:turni.players()){
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
    	return this.turni.nextPlayer();
    }
    
    /**
     * Sposta il giocatore player nel settore se la mossa è valida
     * @param player
     * @param settore
     */
    private void move(Player player, Settore settore){
    	if(!player.isMoveValid(positions.get(player), settore)) throw new IllegalMoveException("Mossa non valida!");
    	this.positions.remove(player);
    	this.positions.put(player, settore);
    	//TODO forse qua sarà inserito un notify alla view
    }
    
    /**
     * Sposta il giocatore player nel settore cui nome viene indicato
     * @param player
     * @param nomeSettore
     */
    public void move(Player player, String nomeSettore){
    	Settore settore = this.tabellone.getSettore(nomeSettore);
    	this.move(player, settore);
    }
    
    /**
     * Registra che il giocatore player ha finito il suo turno
     */
    public void finishTurn(Player player){
    	this.turni.finishTurn(player);
    }
    
    /**
     * Gestisce un turno
     */
    public void manageRound(){
    	//TODO
    }

	/**
	 * @return mazzo di carte settore
	 */
    public Mazzo mazzoDiCarteSettore() {
		return this.mazzoDiCarteSettore;
	}
    
}
