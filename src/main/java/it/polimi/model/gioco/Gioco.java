package it.polimi.model.gioco;

import it.polimi.common.observer.BaseObservable;
import it.polimi.model.carta.Carta;
import it.polimi.model.carta.Mazzo;
import it.polimi.model.exceptions.IllegalMoveException;
import it.polimi.model.exceptions.MazzoVuotoException;
import it.polimi.model.player.AzioneGiocatore;
import it.polimi.model.player.Player;
import it.polimi.model.player.PlayerFactory;
import it.polimi.model.player.Razza;
import it.polimi.model.sector.Settore;
import it.polimi.model.tabellone.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Gioco extends BaseObservable {

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
        this.positions = new HashMap<Player,Settore>();
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
     * @return il giocatore corrente
     */
    public Player currentPlayer(){
    	return this.turni.currentPlayer();
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
     * @param player
     * @return lista di azioni valide per il giocatore
     */
    public List<AzioneGiocatore> getValidActionsForPlayer(Player player){
        List<AzioneGiocatore> listAzioni = new ArrayList<AzioneGiocatore>();
        if(this.positions.get(player).isPericoloso()) listAzioni.add(AzioneGiocatore.PESCA_CARTA);
        if(player.isAlien()) listAzioni.add(AzioneGiocatore.ATTACCA);
        return listAzioni;
    }
    
    /**
     * Registra che il giocatore player ha finito il suo turno
     */
    public void finishTurn(Player player){
    	this.turni.finishTurn(player);
    }
    
    /**
     * Fa prendere al giocatore player una carta dal mazzo di carte settore
     * @param player
     */
    public void pescaCartaSettore(Player player) {
		if(this.mazzoDiCarteSettore.isEmpty()) this.ricostruisciMazzoCarteSettore();
		Carta carta = player.pescaCarta(this.mazzoDiCarteSettore);
		//TODO notify da mettere per comunicare la carta pescata
		
	}

	/**
	 * Ricostruisce il mazzo di carte settore con le carte già
	 * utilizzate dai giocatori
	 */
    private void ricostruisciMazzoCarteSettore() {
		for(Player player : this.positions.keySet()){
			this.mazzoDiCarteSettore.addMazzo(player.mazzo());
		}
		this.mazzoDiCarteSettore.rimischia();		
	}

	/**
	 * @return mazzo di carte settore
	 */
    public Mazzo mazzoDiCarteSettore() {
		return this.mazzoDiCarteSettore;
	}
    
}
