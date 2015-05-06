package it.polimi.controller;

import it.polimi.common.observer.BaseObservable;
import it.polimi.common.observer.BaseObserver;
import it.polimi.common.observer.Event;
import it.polimi.model.Model;
import it.polimi.model.exceptions.IllegalObservableForController;
import it.polimi.model.exceptions.UnknownEventForController;
import it.polimi.view.View;

public class Controller implements BaseObserver {

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
	public void notify(BaseObservable source, Event event) {
		// TODO Auto-generated method stub
		if(!(source instanceof View)) throw new IllegalObservableForController(String.format("%s non è un observable valido per Classe Controller", source.getClass().getName()));
		switch(event.name()){
			case "UserMoveEvent":
				break;
			case "UserSelectedActionEvent":
				break;
			default:
				throw new UnknownEventForController(String.format("Evento %s non riconosciuto da Controller",event.name()));
		}
	}

}
