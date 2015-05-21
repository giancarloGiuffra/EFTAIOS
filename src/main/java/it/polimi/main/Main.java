package it.polimi.main;

import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.controller.Controller;
import it.polimi.model.Model;
import it.polimi.view.View;


public class Main {

	private Model model;
	private View view;
	private Controller controller;
	
	/**
	 * Costruttore
	 */
	private Main() {
	    
		this.model = new Model(3); //TODO dovrà essere modificato per gestire nro giocatore a seconda degli utenti connessi
		this.view = new View(System.in, System.out); //NOSONAR si vuole usare System.out per interagire con l'utente
		this.controller = new Controller(this.model, this.view);
		view.addObserver(controller);
		model.addObserver(controller);
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
	private void run() {		
		(new Thread(view)).start();		
	}

}
