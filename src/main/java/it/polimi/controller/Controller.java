package it.polimi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.common.observer.BaseObservable;
import it.polimi.common.observer.BaseObserver;
import it.polimi.common.observer.Event;
import it.polimi.common.observer.ModelMoveDoneEvent;
import it.polimi.common.observer.UserMoveEvent;
import it.polimi.model.Model;
import it.polimi.model.exceptions.GameException;
import it.polimi.model.exceptions.IllegalObservableForController;
import it.polimi.model.exceptions.UnknownEventForController;
import it.polimi.model.player.AzioneGiocatore;
import it.polimi.view.View;

public class Controller implements BaseObserver { 
	
	//TODO 
	/*valutare l'idea di far diventare il controller observer del model al posto
	 * della view, forse si riesce a coordinare meglio l'iterazione tra la view
	 * e il model
	 */
	
	/*
	 * i metodi chiamati dal model e dall view devono essere racchiusi in un try/catch block
	 * per permettere al controller di gestirle. prima le lasciamo generali, del tipo GameException
	 * e poi quando facciamo le prove li modifichiamo per gestire sole le exception che possono
	 * essere lanciate. Forse model non dovrebbe lanciare exception , pensarci 
	 */
	
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
    		        break;
    		    case "ModelDichiaratoSilenzioEvent": case "ModelAnnunciatoSettoreEvent":
    		        break;
    		    case "ModelCartaAnnunciaSettoreQualunqueEvent":
    		        break;
    		    default:
    		        throw new UnknownEventForController(String.format("Evento %s non riconosciuto da Controller",event.name()));
		    }
		}
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
		} catch (GameException ex){
			LOGGER.log(Level.SEVERE, ex.getMsg(), ex);
		}
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
		try{
			this.view.chiediSettoreDaAnnunciare();
		} catch (GameException ex){
			LOGGER.log(Level.SEVERE, ex.getMsg(), ex);
		}
	}

}
