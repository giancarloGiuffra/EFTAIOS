package it.polimi.gioco;

public class Principale {

	public static void main(String[] args) {
		
		/* Vengono istanziati gli oggetti della classe "Partita" e della classe "Turno".
		 * Finché almeno una delle condizioni di fine partita non è verificata, il ciclo 
		 * "while" provvederà allo svolgimento di ogni singolo turno.
		 */
		Partita nuovaPartita = new Partita(7);  
		Turno nuovoTurno = new Turno(nuovaPartita.getNumeroGiocatori(), nuovaPartita.getListaGiocatori());
		while (nuovaPartita.endGame(nuovoTurno.getTurniGiocati()) == false) {
			nuovoTurno.svolgimentoTurno();
			nuovaPartita.endGame(nuovoTurno.getTurniGiocati());
		}
		
	}

}
