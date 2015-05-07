package it.polimi.model.gioco;

import it.polimi.model.player.Player;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Classe per gestire i turni
 *
 */
public class Turno {

	private final int nro_max_turni = 39;
	private int turn_counter = 0;
	private final int nro_players;
	private Queue<Player> players;
	
	/**
	 * Costruttore
	 * @param listOfPlayers
	 */
	public Turno(List<Player> listOfPlayers) {
		Collections.shuffle(listOfPlayers); //primo giocatore random
		this.players = new LinkedList<Player>(listOfPlayers);
		this.nro_players = this.players.size();
	}
	
	/**
	 * @return lista di giocatori
	 */
	public List<Player> players(){
		return this.players();
	}
	
	/**
	 * Registra che il turno del giocatore player è finito
	 * @param player
	 */
	public void finishTurn(){
		this.players.add(this.players.remove());
		this.turn_counter++;
	}
	
	/**
	 * Controlla se i turni sono finiti
	 * @return true in quel caso
	 */
	public boolean turnsOver(){
		return this.turn_counter == this.nro_max_turni*this.nro_players;
	}
	
	/**
	 * @return il turno corrente, cioè il giro corrente
	 */
	public int currentTurn(){
		return this.turn_counter/this.nro_players+1;
	}

	/**
	 * restituisce il giocatore corrente
	 * @return
	 */
	public Player currentPlayer() {
		return this.players.peek();
	}

}
