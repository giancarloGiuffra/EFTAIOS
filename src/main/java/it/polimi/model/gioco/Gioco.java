package it.polimi.model.gioco;

import it.polimi.common.observer.BaseObservable;
import it.polimi.common.observer.ModelAnnunciatoSettoreEvent;
import it.polimi.common.observer.ModelAttaccoEvent;
import it.polimi.common.observer.ModelCartaAnnunciaSettoreQualunqueEvent;
import it.polimi.common.observer.ModelCartaPescataEvent;
import it.polimi.common.observer.ModelDichiaratoSilenzioEvent;
import it.polimi.common.observer.ModelGameContinues;
import it.polimi.common.observer.ModelGameOver;
import it.polimi.common.observer.ModelMoveDoneEvent;
import it.polimi.model.carta.Carta;
import it.polimi.model.carta.Mazzo;
import it.polimi.model.exceptions.FalsoGameOver;
import it.polimi.model.exceptions.IllegalAzioneGiocatoreException;
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
    private List<Player> playersMorti = new ArrayList<Player>();
    
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
     * 
     * @return il nome del giocatore corrente
     */
    public String currentPlayerName(){
        return this.currentPlayer().nome();
    }
    
    /**
     * 
     * @return il turno, cioè giro, corrente
     */
    public int currentTurnNumber(){
        return this.turni.currentTurn();
    }
    
    /**
     * lista dei giocatori
     * @return
     */
    public List<Player> players(){
        return this.turni.players();
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
    	if(this.isMoveValid(player, settore)){
    	this.positions.remove(player);
    	this.positions.put(player, settore);
    	this.notify(new ModelMoveDoneEvent(settore.getNome()));
    	}
    }
    
    /**
     * Controlla se lo spostamento è valido per il player
     * @param player
     * @param settore
     * @return
     */
    private boolean isMoveValid(Player player, Settore settore){
        return player.isMoveValid(positions.get(player), settore) &&
                this.tabellone.esisteSentieroValido(positions.get(player), settore);
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
        if(player.isAlien() && positions.get(player).isSicuro()) listAzioni.add(AzioneGiocatore.NON_ATTACCA);
        return listAzioni;
    }
    
    /**
     * Registra che il giocatore corrente ha finito il suo turno
     */
    public void finishTurn(){
    	this.turni.finishTurn();
    	this.checkIfGameOver();
    }
    
    /**
     * Controlla se il gioco è finito
     */
    private void checkIfGameOver() {
        if(this.isUmanoInScialuppa()){
            this.notify(new ModelGameOver(this.umanoInScialuppa()));
        } else if(this.isUmaniMorti()){
            this.notify(new ModelGameOver(TipoGameOver.UMANI_MORTI));
        } else if(this.isTurniFiniti()){
            this.notify(new ModelGameOver(TipoGameOver.TURNI_FINITI));
        } else {
            this.notify(new ModelGameContinues("Il gioco continua..."));
        }
    }

    private Player umanoInScialuppa() {
        for(Player player : this.positions.keySet()){
            if(player.isHuman() && this.positions.get(player).isScialuppa()) return player;
        }
        throw new FalsoGameOver("La funzione umanoInScialuppa è stata chiamata erroneamente");
    }

    /**
     * Controlla se uno degli umani è arrivato a una scialuppa
     * @return
     */
    private boolean isUmanoInScialuppa() {
        for(Player player : this.positions.keySet()){
            if(player.isHuman() && this.positions.get(player).isScialuppa()) return true;
        }
        return false;
    }

    /**
     * Controlla se tutti gli umani sono morti
     * @return
     */
    private boolean isUmaniMorti() {
        for(Player player : this.positions.keySet()){
            if(player.isHuman()) return false;
        }
        return true;
    }

    /**
     * Verifica se i turno sono finiti
     * @return
     */
    private boolean isTurniFiniti() {
        return this.turni.turnsOver();
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
		player.salvaCarta(carta); //salviamo nel mazzo del giocatore prima di lanciare il notify per semplicità di gestione
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
    	for(Player possibileVittima : new ArrayList<Player>(this.positions.keySet())){ //to avoid ConcurrentModificationException
    		if(this.positions.get(possibileVittima).equals(this.positions.get(player)) &&
    				!player.equals(possibileVittima)){
    		    possibileVittima.muore();
    			this.positions.remove(possibileVittima);
    			this.turni.remove(possibileVittima);
    			playersMorti.add(possibileVittima);
    		}
    	}
    	this.playersMorti.addAll(playersMorti);
    	this.notify(new ModelAttaccoEvent(player, this.positions.get(player), playersMorti));
    }
    
    /**
     * restituisce lista con i giocatori morti
     * @return
     */
    public List<Player> getListaGiocatoriMorti() {
    	return this.playersMorti;
    }
    
    /**
     * restituisce il tabellone
     * @return
     */
    public Tabellone tabellone(){
        return this.tabellone;
    }
    
}

