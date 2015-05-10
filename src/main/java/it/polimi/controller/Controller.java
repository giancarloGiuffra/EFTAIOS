package it.polimi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.common.observer.BaseObservable;
import it.polimi.common.observer.BaseObserver;
import it.polimi.common.observer.Event;
import it.polimi.common.observer.ModelAnnunciatoSettoreEvent;
import it.polimi.common.observer.ModelAttaccoEvent;
import it.polimi.common.observer.ModelCartaPescataEvent;
import it.polimi.common.observer.ModelMoveDoneEvent;
import it.polimi.common.observer.UserAnnounceSectorEvent;
import it.polimi.common.observer.UserMoveEvent;
import it.polimi.model.Model;
import it.polimi.model.carta.Carta;
import it.polimi.model.exceptions.BadSectorPositionNameException;
import it.polimi.model.exceptions.GameException;
import it.polimi.model.exceptions.IllegalMoveException;
import it.polimi.model.exceptions.IllegalObservableForController;
import it.polimi.model.exceptions.InvalidSectorForAnnouncement;
import it.polimi.model.exceptions.UnknownEventForController;
import it.polimi.model.player.AzioneGiocatore;
import it.polimi.view.View;

public class Controller implements BaseObserver { 
	
	private static final Logger LOGGER = Logger.getLogger(Controller.class.getName());
	private Model model;
	private View view;
	
	/**
	 * Costruttore
	 * @param model
	 * @param view
	 */
	public Controller(Model model, View view){
		this.model = model;
		this.view = view;
	}
	
	@Override
	public void notifyRicevuto(BaseObservable source, Event event) {
		if(!(source instanceof View) && !(source instanceof Model)) throw new IllegalObservableForController(String.format("%s non è un observable valido per Classe Controller", source.getClass().getName()));
		if(source instanceof View){
    		switch(event.name()){
    			case "UserStartEvent":
    				this.startTurn();
    				break;
    			case "UserMoveEvent":
    				this.moveCurrentPlayer( ((UserMoveEvent) event).settoreDestinazione() );
    				break;
    			case "UserPicksCardEvent":
    				this.currentPlayerPescaCartaSettore();
    				break;
    			case "UserAttackEvent":
    				this.currentPlayerAttacca();
    				break;
    			case "UserAnnounceSectorEvent":
    			    this.currentPlayerAnnunciaSettore( ( (UserAnnounceSectorEvent) event).settoreDaAnnunciare() );
    			    break;
    			case "UserTurnoFinitoEvent":
    				this.finishTurn();
    				this.startTurn();
    				break;
    			default:
    				throw new UnknownEventForController(String.format("Evento %s non riconosciuto da Controller",event.name()));
    		}		
		} else { //i.e. source instanceof Model
		    switch(event.name()){
    		    case "ModelMoveDoneEvent":
    		        this.comunicaSpostamento( ( (ModelMoveDoneEvent) event).settoreDestinazione());
    		        this.chiediAzione(this.getValidActionsForCurrentPlayer());
    		        break;
    		    case "ModelCartaPescataEvent":
    		        this.comunicaCartaPescata( ( (ModelCartaPescataEvent) event).carta().nome() );
    		        this.currentPlayerUsaCarta( ( (ModelCartaPescataEvent) event).carta() );
    		        break;
    		    case "ModelDichiaratoSilenzioEvent":
    		        this.comunicaSilenzioDichiarato();
    		        this.comunicaTurnoFinito();
    		        break;
    		    case "ModelAnnunciatoSettoreEvent":
    		        this.comunicaSettoreAnnunciato( ( (ModelAnnunciatoSettoreEvent) event).settore() );
    		        this.comunicaTurnoFinito();
    		        break;
    		    case "ModelCartaAnnunciaSettoreQualunqueEvent":
    		        this.chiediSettoreDaAnnunciare();
    		        break;
    		    case "ModelAttaccoEvent":
    		        this.comunicaAttaccoEffettuato( (ModelAttaccoEvent) event );
    		        this.comunicaTurnoFinito();
    		        break;
    		    default:
    		        throw new UnknownEventForController(String.format("Evento %s non riconosciuto da Controller",event.name()));
		    }
		}
	}
	
	/**
	 * Comunica al giocatore l'attacco effettuato e la lista di morti
	 */
	private void comunicaAttaccoEffettuato(ModelAttaccoEvent event) {
        this.view.print(event.getMsg());
    }

    /**
	 * Fa annunciare al giocatore il settore
	 * @param settoreAnnunciato
	 */
	private void currentPlayerAnnunciaSettore(String settore) {
        try{
            this.model.currentPlayerAnnunciaSettore(settore);
        } catch (BadSectorPositionNameException | InvalidSectorForAnnouncement ex){
            LOGGER.log(Level.SEVERE, ex.getMsg(), ex);
            this.comunicaMessaggio(ex.getMsg());
            this.chiediSettoreDaAnnunciare();
        } catch (GameException ex){
            LOGGER.log(Level.SEVERE, ex.getMsg(), ex);
        }
    }

    /**
     * Comunica messaggio al giocatore
     * @param msg
     */
	private void comunicaMessaggio(String msg) {
        this.view.print(msg);
    }

    /**
	 * Comunica al giocatore che il settore è stato annunciato
	 * @param settore
	 */
	private void comunicaSettoreAnnunciato(String settore) {
        this.view.comunicaSettoreAnnunciato(settore);
    }

    /**
	 * Comunica al giocatore che il suo turno è finito
	 */
	private void comunicaTurnoFinito() {
        this.view.comunicaTurnoFinito();
    }

    /**
	 * Comunica al giocatore che ha dichiarato silenzio
	 */
	private void comunicaSilenzioDichiarato() {
        this.view.comunicaSilenzioDichiarato();
    }

    /**
	 * Fa usare la carta al giocatore corrente
	 * @param carta
	 */
	private void currentPlayerUsaCarta(Carta carta) {
        try{
            this.model.currentPlayerUsaCarta(carta);
        } catch (GameException ex){
            LOGGER.log(Level.SEVERE, ex.getMsg(), ex);
        }
    }

    /**
	 * Communica carta pescata al giocatore
	 * @param carta
	 */
	private void comunicaCartaPescata(String nomeCarta) {
        this.view.comunicaCartaPescata(nomeCarta);
    }

    /**
	 * comunica spostamento effettuato al giocatore
	 * @param settoreDestinazione
	 */
	private void comunicaSpostamento(String settore) {
        this.view.comunicaSpostamento(settore);     
    }

    /**
	 * Fa attaccare al giocatore corrente
	 */
	private void currentPlayerAttacca() {
		try{
			this.model.currentPlayerAttacca();
		} catch (GameException ex){
			LOGGER.log(Level.SEVERE, ex.getMsg(), ex);
		}
	}

	/**
     * Inizia un turno
     */
    private void startTurn(){
    	this.view.print("Tocca a te.");
        this.view.chiediMossa(); //bisogna capire poi come viene girata alla view corrispondente al giocatore currentPlayer
    }

	/**
	 * Finisce il turno
	 */
	private void finishTurn() {
		try {
			this.model.finishTurn();
		} catch (GameException ex){
			LOGGER.log(Level.SEVERE, ex.getMsg(), ex);
		}
	}
	
	/**
	 * Muove il giocatore corrente nel settore indicato
	 * @param nomeSettore
	 */
	private void moveCurrentPlayer(String nomeSettore){
		try {
			this.model.moveCurrentPlayer(nomeSettore);
		} catch(IllegalMoveException|BadSectorPositionNameException ex){
		    LOGGER.log(Level.SEVERE, ex.getMsg(), ex);
		    this.comunicaMessaggio(ex.getMsg());
		    this.chiediMossa();
		} catch (GameException ex){
			LOGGER.log(Level.SEVERE, ex.getMsg(), ex);
		}
	}
	
	/**
	 * Chiedi mossa al giocatore
	 */
	private void chiediMossa() {
        this.view.chiediMossa();
    }

    /**
	 * 
	 * @return lista di azioni valide per giocatore corrente
	 */
	private List<AzioneGiocatore> getValidActionsForCurrentPlayer(){
		List<AzioneGiocatore> lista = new ArrayList<AzioneGiocatore>();
		try{
			lista =  this.model.getValidActionsForCurrentPlayer();
		} catch (GameException ex){
			LOGGER.log(Level.SEVERE, ex.getMsg(), ex);
		}
		return lista;
	}
	
	/**
	 * chiede al giocatore di scegliere un'azione da eseguire
	 * @param azioni
	 */
	private void chiediAzione(List<AzioneGiocatore> azioni){
		try{
			this.view.chiediAzione(azioni);
		} catch (GameException ex){
			LOGGER.log(Level.SEVERE, ex.getMsg(), ex);
		}
	}
	
	/**
	 * fa che il giocatore corrente peschi una carta settore
	 * ed esegua l'azione corrispondente
	 */
	private void currentPlayerPescaCartaSettore(){
		try{
			this.model.currentPlayerPescaCartaSettore();
		} catch (GameException ex){
			LOGGER.log(Level.SEVERE, ex.getMsg(), ex);
		}
	}
	
	/**
	 * chiede al giocatore un settore da annunciare 
	 */
	private void chiediSettoreDaAnnunciare(){
			this.view.chiediSettoreDaAnnunciare();
	}

}
