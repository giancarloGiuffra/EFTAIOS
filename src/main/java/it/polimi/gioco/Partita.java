package it.polimi.gioco;

import java.util.ArrayList;
import it.polimi.model.player.Player;
import it.polimi.model.player.PlayerFactory;
import it.polimi.model.gioco.Gioco;

public class Partita {
	
	private int numeroGiocatori;
	private int umaniInGioco = 0;   //prende il numero da "listaGiocatori" e decresce ad ogni uccisione
	private ArrayList<Player> listaGiocatori;
	private ArrayList<Player> listaGiocatoriMorti; 
	private boolean terminePartita = false; 
	private final int numeroMassimoTurni = 39;
	private possibiliVincitori vincitorePartita;
	
	private enum possibiliVincitori {
		ALIENI, CAPITANO, PILOTA, PSICOLOGO, SOLDATO
	}
	
	public Partita (int numeroGiocatori) {
		/* vedere classe "Personaggio": una volta noto il numero dei giocatori partecipanti alla
		 * partita (verrà inserito dall'utente all'inizio di questa) viene creata una lista
		 * dei giocatori a partire dai primi "n" elementi dichiarati in "Personaggio" 
		 */
		this.numeroGiocatori = numeroGiocatori;
		listaGiocatori = new ArrayList<Player>();
		listaGiocatoriMorti = new ArrayList<Player>();
		listaGiocatori = (ArrayList<Player>) PlayerFactory.createPlayers(numeroGiocatori);
		for (int i = 0; i < listaGiocatori.size(); i++) {
			if (listaGiocatori.get(i).isHuman() == true) {
				umaniInGioco++;
			}
		}
		// FIN QUI FUNZIONA
		
		/*Player giocatoreDiProva;  // eliminare
		for (int i = 0; i < listaGiocatori.size(); i++) {
			giocatoreDiProva = listaGiocatori.get(i);
			System.out.println(giocatoreDiProva.personaggio());
		}*/
		//this.listaGiocatori = listaGiocatori;
	}
	
	public ArrayList<Player> getListaGiocatori() {
		return listaGiocatori; 
	}
	
	public int getNumeroGiocatori() {
		return numeroGiocatori;
	}
	
	public ArrayList<Player> aggiornaListaGiocatori() {
		Gioco gioco = new Gioco(numeroGiocatori);
		listaGiocatoriMorti = (ArrayList<Player>) gioco.getListaGiocatoriMorti();
		for (int i = 0; i < listaGiocatori.size(); i++) {
			for (int j = 0; j < listaGiocatoriMorti.size(); j++) {
				if (listaGiocatori.get(i) == listaGiocatoriMorti.get(j)) {
					listaGiocatori.remove(i);
					umaniInGioco--;
				}
			}
		}
		return listaGiocatori;
	} 
	
	
	public int contaUmaniInGioco() {
		return this.umaniInGioco;
	}
	
	
	/* il metodo "endGame" verificherà il soddisfacimento di ciascuna delle 3 condizioni
	 * che determinano la fine di una partita. Il metodo richiede come parametro di ingresso
	 * il numero di turni giocati (che gli verrà poi fornito dal metodo "getTurniGiocati"
	 * della classe "Turno") e il numero di giocatori umani ancora presenti sul tabellone
	 */
	public boolean endGame(int turniGiocati, int umaniInGioco, boolean umanoSuScialuppa){    
		if (turniGiocati == numeroMassimoTurni) {
			terminePartita = true;
			vincitorePartita = possibiliVincitori.ALIENI;
		}
		if (umaniInGioco == 0) {
			terminePartita = true;
			vincitorePartita = possibiliVincitori.ALIENI;
		}
		if (umanoSuScialuppa == true) { 
			terminePartita = true;
			// solo l'umano è il vincitore
		} 
		return terminePartita;
	}
	
	public possibiliVincitori vincitore(){
		System.out.println("Vincitore: " + vincitorePartita.toString());  // eliminare
		return vincitorePartita;
	}
	
}

