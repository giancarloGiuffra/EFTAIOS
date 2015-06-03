package it.polimi.model.gioco;

import it.polimi.model.player.Player;
import it.polimi.model.player.PlayerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Classe per gestire i turni
 *
 */
public class Turno {

	private static final int MAX_TURNI = 1;
	private int turn_counter = 1;
	private Player firstPlayer;
	private Queue<Player> players;
    private List<Player> playersInStandBy;
	
	/**
	 * Costruttore
	 * @param listOfPlayers
	 */
	public Turno(List<Player> listOfPlayers) {
		Collections.shuffle(listOfPlayers); //primo giocatore random
		this.players = new LinkedList<Player>(listOfPlayers);
		this.firstPlayer = this.players.peek();
		this.playersInStandBy = new ArrayList<Player>();
	}
	
	/**
	 * Copy Constructor
	 * @param turno
	 */
	public Turno(Turno source){
		this.turn_counter = source.turn_counter;
		this.firstPlayer = PlayerFactory.copyPlayer(source.firstPlayer);
		this.players = new LinkedList<Player>(PlayerFactory.copyListOfPlayers(source.players()));
		this.playersInStandBy = new ArrayList<Player>(PlayerFactory.copyListOfPlayers(source.playersInStandBy()));
	}

    /**
	 * @return lista di giocatori
	 */
	public List<Player> players(){
		return new ArrayList<Player>(this.players);
	}
	
	public List<Player> playersInStandBy(){
        return new ArrayList<Player>(this.playersInStandBy);
    }
	
	/**
	 * Registra che il turno del giocatore player è finito
	 * @param player
	 */
	public void finishTurn(){
		this.players.add(this.players.remove());
		if(this.players.peek().equals(firstPlayer)) this.turn_counter++;
	}
	
	/**
	 * Controlla se i turni sono finiti
	 * @return true in quel caso
	 */
	public boolean turnsOver(){
		return this.turn_counter > MAX_TURNI;
	}
	
	/**
	 * @return il turno corrente, cioè il giro corrente
	 */
	public int currentTurn(){
		return this.turn_counter;
	}

	/**
	 * restituisce il giocatore corrente
	 * @return
	 */
	public Player currentPlayer() {
		return this.players.peek();
	}
	
	/**
	 * Elimina player del gioco perchè non ci sarà nei turno successivi
	 * @param player
	 */
	public void remove(Player player){
		if(player.equals(firstPlayer)) this.firstPlayer = this.getNextFirstPlayer();
		this.players.remove(player);
	}

	/**
	 * restituisce il giocatore successivo al giocatore che inizia i turni (i.e. firstPlayer)
	 * @return
	 */
	private Player getNextFirstPlayer() {
		Iterator<Player> iterator = this.players.iterator();
		while(iterator.hasNext()){
			Player player = iterator.next();
			if(player.equals(firstPlayer) && iterator.hasNext()) return iterator.next();
			else return this.players.peek();
		}
		return this.players.peek(); //viene richiesto se no non compila
	}
	
	/**
	 * restituisce il numero massimo di turni
	 * @return
	 */
	public Integer numeroMassimoDiTurni(){
		return MAX_TURNI;
	}
	
	/**
     * mette al giocatore corrent in standby
     */
    public void putCurrentPlayerToSleep() {
        if(this.currentPlayer().equals(firstPlayer)) this.firstPlayer = this.getNextFirstPlayer();
        this.playersInStandBy.add(this.players.remove());
    }

    public Boolean isThisLastPlayerDisconnecting() {
        return this.players.isEmpty();
    }


}