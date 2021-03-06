package it.polimi.model.gioco;

import it.polimi.model.carta.Carta;
import it.polimi.model.carta.Mazzo;
import it.polimi.model.player.Player;
import it.polimi.model.player.PlayerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Classe per gestire i turni
 *
 */
public class Turno {

	private static final int MAX_TURNI = 39;
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
		this.players = new LinkedList<Player>(PlayerFactory.copyListOfPlayers(source.players()));
		this.playersInStandBy = new ArrayList<Player>(PlayerFactory.copyListOfPlayers(source.playersInStandBy()));
		for(Player player : this.players){
		    if(player.equals(source.firstPlayer))
		        this.firstPlayer = player;
		}
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
	protected Player getNextFirstPlayer() {
		Iterator<Player> iterator = this.players.iterator();
		while(iterator.hasNext()){
			Player player = iterator.next();
			if(player.equals(firstPlayer) && iterator.hasNext()) return iterator.next();
		}
		return this.players.peek(); //se arriva fin quà vuol dire che firstPlayer si trovava come ultimo elemento
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
		if(!this.players.isEmpty()) this.playersInStandBy.add(this.players.remove());
	}

	public Boolean isThisLastPlayerDisconnecting() {
		return this.players.isEmpty();
	}
	
	@Override
    public int hashCode() {
	    List<Player> thisList = new ArrayList<Player>(this.players);
        thisList.addAll(this.playersInStandBy);
        thisList.add(this.firstPlayer);
        HashCodeBuilder hash =  new HashCodeBuilder(23, 1063);
        for(Player player : thisList)
                hash.append(player);
        return hash.toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Turno other = (Turno) obj;
        List<Player> thisList = new ArrayList<Player>(this.players);
        List<Player> otherList = new ArrayList<Player>(other.players);
        thisList.addAll(this.playersInStandBy);
        otherList.addAll(other.playersInStandBy);
        thisList.add(this.firstPlayer);
        otherList.add(other.firstPlayer);
        return new EqualsBuilder().
                append(thisList, otherList).
                isEquals();
    }

}
