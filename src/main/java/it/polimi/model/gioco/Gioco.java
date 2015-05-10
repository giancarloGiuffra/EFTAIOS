package it.polimi.model.gioco;

import it.polimi.common.observer.BaseObservable;
import it.polimi.common.observer.ModelAnnunciatoSettoreEvent;
import it.polimi.common.observer.ModelAttaccoEvent;
import it.polimi.common.observer.ModelCartaAnnunciaSettoreQualunqueEvent;
import it.polimi.common.observer.ModelCartaPescataEvent;
import it.polimi.common.observer.ModelDichiaratoSilenzioEvent;
import it.polimi.common.observer.ModelMoveDoneEvent;
import it.polimi.model.carta.Carta;
import it.polimi.model.carta.Mazzo;
import it.polimi.model.exceptions.IllegalAzioneGiocatoreException;
import it.polimi.model.exceptions.IllegalMoveException;
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
     * @return il giocatore corrente
     */
    private Player currentPlayer(){
    	return this.turni.currentPlayer();
    }
    
    /**
     * Sposta il giocatore corrente nella posizione indicata
     * @param nomeSettore
     */
    public void moveCurrentPlayer(String nomeSettore){
        this.move(this.currentPlayer(), nomeSettore);
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
    	this.notify(new ModelMoveDoneEvent(settore.getNome()));
    }
    
    /**
     * Sposta il giocatore player nel settore cui nome viene indicato
     * @param player
     * @param nomeSettore
     */
    private void move(Player player, String nomeSettore){
    	Settore settore = this.tabellone.getSettore(nomeSettore);
    	this.move(player, settore);
    }
    
    /**
     * @return lista di azioni valide per giocatore corrente
     */
    public List<AzioneGiocatore> getValidActionsForCurrentPlayer(){
        return this.getValidActionsForPlayer(this.currentPlayer());
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
     * Registra che il giocatore corrente ha finito il suo turno
     */
    public void finishTurn(){
    	this.turni.finishTurn();
    }
    
    /**
     * Fa pescare carta al giocatore corrente
     */
    public void currentPlayerPescaCartaSettore(){
        this.pescaCartaSettore(this.currentPlayer());
    }
    
    /**
     * Fa prendere al giocatore player una carta dal mazzo di carte settore
     * @param player
     */
    private void pescaCartaSettore(Player player) {
		if(this.mazzoDiCarteSettore.isEmpty()) this.ricostruisciMazzoCarteSettore();
		Carta carta = player.pescaCarta(this.mazzoDiCarteSettore);
		this.notify(new ModelCartaPescataEvent(carta));
	}
    
    /**
     * Fa usare la carta al player corrente
     * @param carta
     */
    public void currentPlayerUsaCarta(Carta carta){
        this.usaCarta(this.currentPlayer(), carta);
    }
    
    /**
     * Fa utilizzare a player la carta
     * @param player
     * @param carta
     */
    private void usaCarta(Player player, Carta carta){
        switch(carta.azione()){
        case ANNUNCIA_SETTORE:
            this.notify(new ModelCartaAnnunciaSettoreQualunqueEvent());
            break;
        case ANNUNCIA_SETTORE_MIO:
            this.annunciaSettore(player, this.positions.get(player));
            break;
        case DICHIARA_SILENZIO:
            this.dichiaraSilenzio(player);
            break;
        default:
            throw new IllegalAzioneGiocatoreException("Azione Giocatore non valida");
        }
    }

	/**
	 * fa dichiarare silenzio a player
	 * @param player
	 */
    private void dichiaraSilenzio(Player player) {
		player.dichiaraSilenzio();
		this.notify(new ModelDichiaratoSilenzioEvent());
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
     * Fa al player annuniciare il settore indicato
     * @param player
     * @param settore
     */
    private void annunciaSettore(Player player, Settore settore){
    	player.annunciaSettore(settore);
    	this.notify(new ModelAnnunciatoSettoreEvent(settore.getNome()));
    }
    
    /**
     * Fa al player annunciare il settore cui nome è indicato
     * @param player
     * @param nomeSettore
     */
    private void annunciaSettore(Player player, String nomeSettore){
    	Settore settore = this.tabellone.getSettore(nomeSettore);
    	this.annunciaSettore(player, settore);
    }
    
    /**
     * Fa annunciare al giocatore corrente il settore indicato
     * @param nomeSettore
     */
    public void currentPlayerAnnunciaSettore(String nomeSettore){
    	this.annunciaSettore(this.currentPlayer(), nomeSettore);
    }
    
    /**
     * Fa attaccare al giocatore corrente
     */
    public void currentPlayerAttacca(){
    	this.attacca(this.currentPlayer());
    }
    
    /**
     * Fa attaccare a player
     * @param player
     * @param settore
     */
    private void attacca(Player player){
    	player.attacca(this.positions.get(player));
    	List<Player> playersMorti = new ArrayList<Player>();
    	for(Player possibileVittima : this.positions.keySet()){
    		if(this.positions.get(possibileVittima) == this.positions.get(player) &&
    				!player.equals(possibileVittima)){
    		    possibileVittima.muore();
    			this.positions.remove(possibileVittima);
    			this.turni.remove(possibileVittima);
    			playersMorti.add(possibileVittima);
    		}
    	}
    	this.notify(new ModelAttaccoEvent(player, this.positions.get(player), playersMorti));
    }
    
}
