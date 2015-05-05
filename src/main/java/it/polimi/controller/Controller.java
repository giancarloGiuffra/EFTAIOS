package it.polimi.controller;

import it.polimi.common.observer.BaseObservable;
import it.polimi.common.observer.BaseObserver;
import it.polimi.common.observer.Event;
import it.polimi.model.Model;
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

	}

}
