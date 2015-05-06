package it.polimi.gioco;

import it.polimi.model.player.Personaggio;

public class Partita {
	
	private int numeroGiocatori;
	// private int umaniInGioco;   prende il numero da listaGiocatori[] e decresce ad ogni uccisione
	private Personaggio listaPersonaggi[] = Personaggio.values();
	private Personaggio listaGiocatori[];
	private boolean terminePartita = false; 
	private final int numeroMassimoTurni = 39;
	
	public Partita (int numeroGiocatori) {
		this.numeroGiocatori = numeroGiocatori;
		listaGiocatori = new Personaggio[numeroGiocatori];
		for (int i = 0; i < numeroGiocatori; i++) {
			listaGiocatori[i] = listaPersonaggi[i];
		}
		
	}
	
	public Personaggio[] getListaGiocatori() {
		return listaGiocatori; 
	}
	
	public int getNumeroGiocatori() {
		return numeroGiocatori;
	}
	
	/* il metodo "endGame" verificherà il soddisfacimento di ciascuna delle 3 condizioni
	 * che determinano la fine di una partita. Il metodo richiede come parametro di ingresso
	 * il numero di turni giocati, che gli verrà poi fornito dal metodo "getTurniGiocati"
	 * della classe "Turno"
	 */
	public boolean endGame(int turniGiocati){
		if (turniGiocati == numeroMassimoTurni) {
			terminePartita = true;
		}
		return terminePartita;
	}
	
}
