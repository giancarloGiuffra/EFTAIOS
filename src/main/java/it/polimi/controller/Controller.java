package it.polimi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.common.logger.FilterAllLogs;
import it.polimi.common.logger.FilterHigherThanInfoLevelLogs; 
import it.polimi.common.observer.BaseObservable;
import it.polimi.common.observer.BaseObserver;
import it.polimi.common.observer.ControllerUpdateModel;
import it.polimi.common.observer.Event;
import it.polimi.common.observer.ModelAnnunciatoSettoreEvent;
import it.polimi.common.observer.ModelAttaccoEvent; 
import it.polimi.common.observer.ModelCartaPescataEvent;
import it.polimi.common.observer.ModelGameOver;
import it.polimi.common.observer.ModelMoveDoneEvent;
import it.polimi.common.observer.UserAnnounceSectorEvent;
import it.polimi.common.observer.UserMoveEvent;
import it.polimi.model.Model;
import it.polimi.model.ModelView;
import it.polimi.model.carta.Carta;
import it.polimi.model.exceptions.BadSectorException; 
import it.polimi.model.exceptions.BadSectorPositionNameException;
import it.polimi.model.exceptions.GameException;
import it.polimi.model.exceptions.IllegalMoveException; 
import it.polimi.model.exceptions.IllegalObservableForController;
import it.polimi.model.exceptions.InvalidSectorForAnnouncement;
import it.polimi.model.exceptions.UnknownEventForController;
import it.polimi.model.player.AzioneGiocatore;
import it.polimi.view.View;

public class Controller extends BaseObservable implements BaseObserver { 
	
	private static final Logger LOGGER = Logger.getLogger(Controller.class.getName());
	private ModelView model;
	private View view;
	
	//static block
	static{
	    LOGGER.setFilter(new FilterAllLogs());
	}
	
	/**
	 * Costruttore
	 * @param model
	 * @param view
	 */
	public Controller(ModelView model, View view){
		this.model = model;
		this.view = view;
	}
	
	@Override
	public void notifyRicevuto(BaseObservable source, Event event) {
		if(!(source instanceof View) && !(source instanceof Model)) throw new IllegalObservableForController(String.format("%s non è un observable valido per Classe Controller", source.getClass().getName()));
		if(source instanceof View){
		    this.gestisceNotifyDaView(event);
    	} else { //i.e. source instanceof Model
    	    this.gestisceNotifyDaModel(event);    
		}
	}
	
	/**
	 * Gestisce evento lanciato da Model
	 * @param event
	 */
	private void gestisceNotifyDaModel(Event event) {
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
            this.comunicaSilenzioDichiarato(event);
            this.comunicaTurnoFinito();
            break;
        case "ModelAnnunciatoSettoreEvent":
            this.comunicaSettoreAnnunciato(event);
            this.comunicaTurnoFinito();
            break;
        case "ModelCartaAnnunciaSettoreQualunqueEvent":
            this.chiediSettoreDaAnnunciare();
            break;
        case "ModelAttaccoEvent":
            this.comunicaAttaccoEffettuato(event);
            this.comunicaTurnoFinito();
            break;
        case "ModelGameOver":
            this.comunicaGiocoFinito(event );
            break;
        case "ModelGameContinues":
            this.startTurn();
            break;
        default:
            throw new UnknownEventForController(String.format("Evento %s non riconosciuto da Controller",event.name()));
	    } 
    }

    /**
	 * Gestisce l'evento lanciato dalla view
	 * @param event
	 */
	private void gestisceNotifyDaView(Event event) {
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
            break;
        default:
            break;
	    }
        
    }

    /**
	 * Comunica che il gioco è finito
	 * @param event
	 */
	private void comunicaGiocoFinito(Event event) {
        this.view.comunicaGiocoFinito(event);
    }

    /**
	 * Comunica al giocatore l'attacco effettuato e la lista di morti
	 */
	private void comunicaAttaccoEffettuato(Event event) {
        this.view.comunicaAttaccoEffettuato(event);
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
        this.view.printFineMessaggio();
    }

    /**
	 * Comunica al giocatore che il settore è stato annunciato
	 * @param event
	 */
	private void comunicaSettoreAnnunciato(Event event) {
        this.view.comunicaSettoreAnnunciato(event);
    }

    /**
	 * Comunica al giocatore che il suo turno è finito
	 */
	private void comunicaTurnoFinito() {
        this.notify(new ControllerUpdateModel(this.model.model()));
		this.view.comunicaTurnoFinito();
    }

    /**
	 * Comunica al giocatore che ha dichiarato silenzio
	 */
	private void comunicaSilenzioDichiarato(Event event) {
        this.view.comunicaSilenzioDichiarato(event);
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
    	this.view.print(String.format("Tocca a te %s - Turno numero %d - Posizione %s", this.currentPlayerName(), this.currentTurnNumber(), this.model.currentPlayerPosition()));
        this.view.chiediMossa(); //bisogna capire poi come viene girata alla view corrispondente al giocatore currentPlayer
    }

	/**
	 * 
	 * @return il numero di turno corrente
	 */
    private int currentTurnNumber() {
        return this.model.currentTurnNumber();
    }

    private String currentPlayerName() {
        return this.model.currentPlayerName();
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
		} catch(BadSectorException | BadSectorPositionNameException ex){
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
