package it.polimi.model.gioco;

import it.polimi.client.NotifierClient;
import it.polimi.common.logger.FilterAllLogs;
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
import it.polimi.model.exceptions.BadSectorException;
import it.polimi.model.exceptions.FalsoGameOver;
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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Gioco extends BaseObservable {

    private static final Logger LOGGER = Logger.getLogger(Gioco.class.getName());
    private final Tabellone tabellone;
    private Mazzo mazzoDiCarteSettore;
    private Map<Player,Settore> positions;
    private Map<Player,Settore> positionsOfPlayersInStandBy;
    private Turno turni; //Per gestire i turni
    
  //static block
    static{
        LOGGER.setFilter(new FilterAllLogs());
    }
    
    /**
     * Costruttore
     * @param numGiocatori numero di giocatori
     */
    public Gioco(int numGiocatori) {
        this.tabellone = TabelloneFactory.createTabellone("GALILEI");
        this.mazzoDiCarteSettore = Mazzo.creaNuovoMazzoCarteSettore();
        this.turni = new Turno(PlayerFactory.createPlayers(numGiocatori));
        this.positions = new HashMap<Player,Settore>();
        this.positionsOfPlayersInStandBy = new HashMap<Player,Settore>();
        for(Player player:turni.players()){
            if(player.razza()==Razza.HUMAN){
                positions.put(player, tabellone.baseUmana());
            }else{
                positions.put(player, tabellone.baseAliena());
            }
        }
    }
    
    /**
     * Copy Constructor
     * @param source
     */
    public Gioco(Gioco source){
    	this.tabellone = new Tabellone(source.tabellone);
    	this.mazzoDiCarteSettore = new Mazzo(source.mazzoDiCarteSettore);
    	this.turni = new Turno(source.turni);
    	this.positions = new HashMap<Player,Settore>();
    	this.positionsOfPlayersInStandBy = new HashMap<Player,Settore>();
    	for(int i = 0; i < this.turni.players().size(); i++)
    		positions.put(this.turni.players().get(i), this.tabellone.getSettore(source.positions.get(source.turni.players().get(i)).getNome()));
    	for(int i = 0; i < this.turni.playersInStandBy().size(); i++)
    		positionsOfPlayersInStandBy.put(this.turni.playersInStandBy().get(i), this.tabellone.getSettore(source.positionsOfPlayersInStandBy.get(source.turni.playersInStandBy().get(i)).getNome()));
    }
    
    /**
     * @return il giocatore corrente
     */
    Player currentPlayer(){
    	return this.turni.currentPlayer();
    }
    
    /**
     * 
     * @return il nome del giocatore corrente
     */
    public String currentPlayerName(){
        if(!isThisLastPlayerDisconnecting())
            return this.currentPlayer().nome();
        else
            return "DEFAULT";
    }
    
    /**
     * nome del settore dove si trova il giocatore corrente
     * @return
     */
    public String currentPlayerPosition(){
        if(!isThisLastPlayerDisconnecting())
            return this.positions.get(this.currentPlayer()).getNome();
        else
            return "DEFAULT";
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
    public void checkIfGameOver() {
        if(this.isUmanoInScialuppa()){
            this.notify(new ModelGameOver(this.umanoInScialuppa()));
        } else if(this.isUmaniMorti()){
            this.notify(new ModelGameOver(TipoGameOver.UMANI_MORTI, this.aliens()));
        } else if(this.isTurniFiniti()){
            this.notify(new ModelGameOver(TipoGameOver.TURNI_FINITI, this.aliens()));
        } else {
            this.notify(new ModelGameContinues("Il gioco continua..."));
        }
    }

    /**
     * 
     * @return lista con i giocatori alien
     */
    private List<Player> aliens() {
        List<Player> aliens = new ArrayList<Player>();
        for(Player player : this.players()){
            if(player.isAlien()) aliens.add(player);
        }
        return aliens;   
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
		player.pescaCarta(this.mazzoDiCarteSettore);
		this.notify(new ModelCartaPescataEvent(player.getLastCard()));
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
		this.notify(new ModelDichiaratoSilenzioEvent(player.nome()));
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
    	this.notify(new ModelAnnunciatoSettoreEvent(settore.getNome(), player.nome()));
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
    	this.notify(new ModelAttaccoEvent(player, this.positions.get(player), playersMorti));
    }
    
    
    /**
     * restituisce il tabellone
     * @return
     */
    public Tabellone tabellone(){
        return this.tabellone;
    }
    
    /**
     * passa il turno del giocatore corrente mettendolo in standby
     * e controllando se il gioco è finito 
     */
    public void putCurrentPlayerToSleep(){
    	if(turni.players().isEmpty()) return;
    	this.positionsOfPlayersInStandBy.put(this.currentPlayer(), this.positions.get(this.currentPlayer()));
    	this.positions.remove(this.currentPlayer());
    	this.turni.putCurrentPlayerToSleep(); //il giocatore fa comunque parte del gioco
    }
    
    /**
     * controlla se non ci sono più giocatori presenti
     * @return
     */
    public Boolean isThisLastPlayerDisconnecting(){
    	return this.turni.isThisLastPlayerDisconnecting();
    }
    
    /**
     * calcola i settori validi per la mossa del giocatore corrente
     * @return
     */
    public List<String> calcolaSettoriValidiForCurrentPlayer(){
        return this.calcolaSettoriValidi(this.currentPlayer());
    }
    
    /**
     * calcola i settori validi per la mossa del player indicato
     * @param player
     * @return
     */
    private List<String> calcolaSettoriValidi(Player player){
        if(player.isHuman()) return this.calcolaSettoriValidiForHuman(player);
        else return this.calcolaSettoriValidiForAlien(player);
    }

    private List<String> calcolaSettoriValidiForAlien(Player player) {
        List<String> settori = new ArrayList<String>();
        for(String settore : this.positions.get(player).getSettoriAdiacentiADistanzaDue()){
            try{
        	if(this.tabellone.getSettore(settore).isValidDestinationForAlien() &&
               this.tabellone.esisteSentieroValido(this.positions.get(player), this.tabellone.getSettore(settore)))
               settori.add(settore);
            } catch (BadSectorException ex){
                LOGGER.log(Level.INFO, "si ignora l'eccezione", ex);
            	//skip
            }
        }
        return settori;
    }

    private List<String> calcolaSettoriValidiForHuman(Player player) {
        List<String> settori = new ArrayList<String>();
        for(String settore : this.positions.get(player).getSettoriAdiacenti()){
            try{
        	if(this.tabellone.getSettore(settore).isValidDestinationForHuman())
                settori.add(settore);
            } catch (BadSectorException ex){
                LOGGER.log(Level.INFO, "si ignora l'eccezione", ex);
            	//skip
            }
        }
        return settori;
    }
    
    /**
     * getter per positions
     */
    public Map<Player,Settore> posizioni(){
        return positions;
    }
    
    /**
     * getter per mazzo di carte settore
     * @return
     */
    public Mazzo mazzoCarteSettore(){
    	return this.mazzoDiCarteSettore;
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(211, 6427).
                append(this.tabellone).
                append(this.mazzoDiCarteSettore).
                append(this.turni).
                append(this.positions).
                append(this.positionsOfPlayersInStandBy).
                toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Gioco))
            return false;
        Gioco other = (Gioco) obj;
        return new EqualsBuilder().
                append(this.tabellone,other.tabellone).
                append(this.mazzoDiCarteSettore, other.mazzoDiCarteSettore).
                append(this.turni, other.turni).
                append(this.positions, other.positions).
                append(this.positionsOfPlayersInStandBy, other.positionsOfPlayersInStandBy).
                isEquals();
    }
}

