package it.polimi.gioco;

import it.polimi.model.player.AlienPlayer;
import it.polimi.model.player.HumanPlayer;
import it.polimi.model.player.Personaggio;
import it.polimi.model.player.Player;

public class Partita {
	
	private int numeroGiocatori;
	private int umaniInGioco = 3;   //prende il numero da listaGiocatori[] e decresce ad ogni uccisione
	private Personaggio listaPersonaggi[] = Personaggio.values();
	private Player listaGiocatori[];
	// private Player listaGiocatoriUmani[]; necessario?
	private boolean terminePartita = false; 
	private final int numeroMassimoTurni = 39;
	
	public Partita (int numeroGiocatori) {
		/* vedere classe "Personaggio": una volta noto il numero dei giocatori partecipanti alla
		 * partita (verrà inserito dall'utente all'inizio di questa) viene creata una lista
		 * dei giocatori a partire dai primi "n" elementi dichiarati in "Personaggio" 
		 */
		this.numeroGiocatori = numeroGiocatori;
		Player listaGiocatori[] = new Player[this.numeroGiocatori];
		// modificare
		for (int i = 0; i < listaGiocatori.length; i++){
			if (listaPersonaggi[i].isHuman() == true) {
				//listaGiocatori[i] = new HumanPlayer(listaPersonaggi[i]);
			}
			else {
				//listaGiocatori[i] = new AlienPlayer(listaPersonaggi[i]);
			}
		}
		this.listaGiocatori = listaGiocatori;
	}
	
	public Player[] getListaGiocatori() {
		return listaGiocatori; 
	}
	
	public int getNumeroGiocatori() {
		return numeroGiocatori;
	}
	
	// NECESSARIO?
	/*public Player[] getListaGiocatoriUmani() {
		int j = 0;  // contatore per listaGiocatoriUmani
		for (int i = 0; i < listaGiocatori.length; i++) {
			if (listaGiocatori[i].isHuman() == true) {
				listaGiocatoriUmani[j] = listaGiocatori[i];
				j++;
			}
		}
		this.umaniInGioco = j;
		return listaGiocatoriUmani;
	} */
	
	/*public Personaggio[] dopoMortePersonaggio() {
		/* alla morte di ogni personaggio viene restituita una nuova lista 
		 * dei giocatori aggiornata
		 *
		
	} */
	
	public int contaUmaniInGioco() {
		return this.umaniInGioco;
	} 
	
	/* il metodo "endGame" verificherà il soddisfacimento di ciascuna delle 3 condizioni
	 * che determinano la fine di una partita. Il metodo richiede come parametro di ingresso
	 * il numero di turni giocati (che gli verrà poi fornito dal metodo "getTurniGiocati"
	 * della classe "Turno") e il numero di giocatori umani ancora presenti sul tabellone
	 */
	public boolean endGame(int turniGiocati, int umaniInGioco){    
		if (turniGiocati == numeroMassimoTurni) {
			terminePartita = true;
		}
		if (umaniInGioco == 0) {
			terminePartita = true;
		}
		return terminePartita;
	}
	
}

