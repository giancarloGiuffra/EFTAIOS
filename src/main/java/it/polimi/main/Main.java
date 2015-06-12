package it.polimi.main;

import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.common.observer.BaseObservable;
import it.polimi.common.observer.BaseObserver;
import it.polimi.common.observer.ControllerUpdateModel;
import it.polimi.common.observer.Event;
import it.polimi.controller.Controller;
import it.polimi.gui.GUI;		// ...
import it.polimi.model.Model;
import it.polimi.model.sector.Settore;
import it.polimi.model.sector.TipoSettore;
import it.polimi.model.ModelView;
import it.polimi.model.exceptions.IllegalObservableForMain;
import it.polimi.view.View;


public class Main implements BaseObserver{

	private Model model;
	private ModelView modelView;
	private View view;
	private Controller controller;
	
	/**
	 * Costruttore
	 */
	public Main(Integer numeroGiocatori, View view) {
		this.model = new Model(numeroGiocatori);
		this.modelView = new ModelView(this.model);
		this.view = view;
		this.controller = new Controller(this.modelView, this.view);
		controller.addObserver(this); //controller notificherà a main per il update di model
		view.addObserver(controller);
		modelView.addObserver(controller);
	}
	
	/**
	 * Costruttore
	 * @param numeroGiocatori
	 */
	private Main(Integer numeroGiocatori){
		this(numeroGiocatori, new View(System.in, System.out));
	}
	
	/**
	 * Costruttore
	 */
	private Main(){
		this(2); // di default due giocatrori
	}

	/**
	 * main
	 * @param args
	 */
	public static void main(String[] args) {
		Main main = new Main();
		main.run();
	}

	/**
	 * inizia il gioco
	 */
	public void run() {		
		(new Thread(view)).start();		
	}	

	@Override
	public void notifyRicevuto(BaseObservable source, Event event) {
		if(!(source instanceof Controller)) throw new IllegalObservableForMain(String.format("%s non è un observable valido per Classe Main", source.getClass().getName()));
		if("ControllerUpdateModel".equals(event.name())) this.model = new Model(((ControllerUpdateModel)event).model());
	}

}
