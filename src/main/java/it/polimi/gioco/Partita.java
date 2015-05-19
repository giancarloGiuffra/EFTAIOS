package it.polimi.gioco;

import java.util.ArrayList;
import it.polimi.model.player.Personaggio;
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
	private ArrayList<Personaggio> vincitorePartita = new ArrayList<Personaggio>();
	
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
	 * che determinano la fine di una partita. 
	 * Il numero di turni giocati viene fornito dal metodo "getTurniGiocati"
	 * della classe "Turno"
	 */
	public boolean endGame(int turniGiocati, int umaniInGioco, boolean umanoSuScialuppa){    
		if (turniGiocati == numeroMassimoTurni) {
			terminePartita = true;
			vincitorePartita = alieniVincitori();
		}
		else if (umaniInGioco == 0) {
			terminePartita = true;
			vincitorePartita = alieniVincitori();
		}
		else if (umanoSuScialuppa == true) { 
			terminePartita = true;
			Gioco gioco = new Gioco(numeroGiocatori);
			vincitorePartita.add(gioco.umanoVincitore().personaggio());
		} 
		return terminePartita;
	}
	
	private ArrayList<Personaggio> alieniVincitori() {
		Personaggio listaPersonaggi[] = Personaggio.values();
		for (int i = 0; i < listaPersonaggi.length; i++) {
			if (listaPersonaggi[i].isAlien() == true) {
				vincitorePartita.add(listaPersonaggi[i]);
			}
		}
		return vincitorePartita;
	}
	
	public ArrayList<Personaggio> vincitore(){
		System.out.println("Vincitore: " + vincitorePartita.toString());  // eliminare
		return vincitorePartita;
	}
	
}

