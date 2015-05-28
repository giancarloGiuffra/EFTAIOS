package it.polimi.gioco;

import it.polimi.controller.Controller;
import it.polimi.model.Model;
import it.polimi.view.View;
import it.polimi.gioco.Partita;
import it.polimi.gioco.Turno;
import it.polimi.gui.*;

public class Principale {
	
	private Model model;
	private View view;
	private Controller controller;
	
	private Principale() {
		this.model = new Model(8); //TODO dovrà essere modificato per gestire nro giocatore a seconda degli utenti connessi
		this.view = new View(System.in, System.out); //NOSONAR si vuole usare System.out per interagire con l'utente
		this.controller = new Controller(this.model, this.view);
		view.addObserver(controller);
		//model.addObserver(view);
	}

	public static void main(String[] args) {
		
		/*Principale main = new Principale();
		main.run();	*/
		GUI nuovaGUI = new GUI();
		nuovaGUI.partecipazionePartita();
		Partita nuovaPartita = new Partita(7); //modificare 
		Turno nuovoTurno = new Turno(nuovaPartita.getNumeroGiocatori(), nuovaPartita.getListaGiocatori());
		while (nuovaPartita.endGame(nuovoTurno.getTurniGiocati(), nuovaPartita.contaUmaniInGioco(), nuovoTurno.umanoSuScialuppa) == false) {
			nuovoTurno.svolgimentoTurno();
			//nuovaPartita.aggiornaListaGiocatori();
			//nuovaPartita.endGame(nuovoTurno.getTurniGiocati(), nuovaPartita.contaUmaniInGioco(), nuovoTurno.umanoSuScialuppa);
		}
		nuovaPartita.vincitore();
		
	}
	
	/*private void run() {		
		(new Thread(view)).start();		
	} */

}
