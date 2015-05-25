package it.polimi.model.gioco;

import it.polimi.model.player.Player;

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

	private static final int MAX_TURNI = 2;
	private int turn_counter = 1;
	private Player firstPlayer;
	private Queue<Player> players;
	
	/**
	 * Costruttore
	 * @param listOfPlayers
	 */
	public Turno(List<Player> listOfPlayers) {
		Collections.shuffle(listOfPlayers); //primo giocatore random
		this.players = new LinkedList<Player>(listOfPlayers);
		this.firstPlayer = this.players.peek();
	}
	
	/**
	 * @return lista di giocatori
	 */
	public List<Player> players(){
		return new ArrayList<Player>(this.players);
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
			if(iterator.equals(firstPlayer)) return iterator.next();
			iterator.next();
		}
		return this.players.peek(); //se ha percorso tutta la queue vuol dire che il next è in testa
	}
	
	/**
	 * restituisce il numero massimo di turni
	 * @return
	 */
	public Integer numeroMassimoDiTurni(){
		return MAX_TURNI;
	}

}
