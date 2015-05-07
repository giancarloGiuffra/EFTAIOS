package it.polimi.controller;

import it.polimi.common.observer.BaseObservable;
import it.polimi.common.observer.BaseObserver;
import it.polimi.common.observer.Event;
import it.polimi.common.observer.UserMoveEvent;
import it.polimi.model.Model;
import it.polimi.model.exceptions.IllegalObservableForController;
import it.polimi.model.exceptions.UnknownEventForController;
import it.polimi.model.player.Player;
import it.polimi.view.View;

public class Controller implements BaseObserver { 
	
	//TODO 
	/*valutare l'idea di far diventare il controller observer del model al posto
	 * della view, forse si riesce a coordinare meglio l'iterazione tra la view
	 * e il model
	 */
	
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
	
	/**
     * Inizia un turno
     */
    private void startTurn(){
    	this.view.chiediMossa(); //bisogna capire poi come viene girata alla view corrispondente al giocatore currentPlayer
    }
	
	@Override
	public void notifyRicevuto(BaseObservable source, Event event) {
		if(!(source instanceof View)) throw new IllegalObservableForController(String.format("%s non è un observable valido per Classe Controller", source.getClass().getName()));
		switch(event.name()){
			case "UserStartEvent":
				this.startTurn();
				break;
			case "UserMoveEvent":
				this.model.moveCurrentPlayer( ((UserMoveEvent) event).settoreDestinazione() );
				this.view.chiediAzione(this.model.getValidActionsForCurrentPlayer());
				break;
			case "UserPicksCardEvent":
				this.model.currentPlayerPescaCartaSettore();
				break;
			case "UserAttackEvent":
				break;
			case "UserTurnoFinitoEvent":
				this.finishTurn();
				this.startTurn();
				break;
			default:
				throw new UnknownEventForController(String.format("Evento %s non riconosciuto da Controller",event.name()));
		}
	}

	/**
	 * Finisce il turno
	 */
	private void finishTurn() {
		this.model.finishTurn();
	}

}
