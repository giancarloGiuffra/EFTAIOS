package it.polimi.gioco;

import it.polimi.controller.Controller;
import it.polimi.model.Model;
import it.polimi.view.View;
import it.polimi.gioco.Partita;
import it.polimi.gioco.Turno;

public class Principale {
	
	private Model model;
	private View view;
	private Controller controller;
	
	private Principale() {
		this.model = new Model(8); //TODO dovrà essere modificato per gestire nro giocatore a seconda degli utenti connessi
		this.view = new View(System.in, System.out); //NOSONAR si vuole usare System.out per interagire con l'utente
		this.controller = new Controller(this.model, this.view);
		view.addObserver(controller);
		model.addObserver(view);
	}

	public static void main(String[] args) {
		
		Principale main = new Principale();
		main.run();	
		
		Partita nuovaPartita = new Partita(7);  // far sì che il numero di giocatori (qui 7) 
		// 										venga inserito dall'utente (rimando ad interfaccia grafica)
		Turno nuovoTurno = new Turno(nuovaPartita.getNumeroGiocatori(), nuovaPartita.getListaGiocatori());
		while (nuovaPartita.endGame(nuovoTurno.getTurniGiocati(), nuovaPartita.contaUmaniInGioco()) == false) {
			nuovoTurno.svolgimentoTurno();
			nuovaPartita.endGame(nuovoTurno.getTurniGiocati(), nuovaPartita.contaUmaniInGioco());
		}
		
	}
	
	private void run() {		
		(new Thread(view)).start();		
	}

}
