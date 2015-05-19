package it.polimi.gioco;

import it.polimi.model.player.Player;
import java.util.ArrayList;
import java.util.Random;

public class Turno {
	
	private int primoGiocatore;
	private ArrayList<Player> listaGiocatori;
	private int turniGiocati = 0;
	boolean umanoSuScialuppa = false;
	
	/* All'inizio di una partita, quando viene inizializzato per la prima volta un oggetto di 
	 * tipo "Turno", viene scelto casualmente il giocatore da cui comincerà il giro
	 */ 
	public Turno(int numeroGiocatori, ArrayList<Player> listaGiocatori) {
		Random random = new Random();
		this.primoGiocatore = random.nextInt(numeroGiocatori); 
		this.listaGiocatori = listaGiocatori;
	}
	
	public void svolgimentoTurno(){  // aggiornare listaGiocatori in caso un giocatore muoia
		int indiceGiocatore = this.primoGiocatore; 
		do {
			//listaGiocatori[j].azione();
			// if (positions.get(player).isScialuppa() == true) // controllo razza non necessario: alieni non possono andare su scialuppe
			
			/* Poiché il ciclo "do-while" esegue le istruzioni prima di verificare la condizione
			 * di controllo, ci si deve accertare di non essere arrivati all'ultimo elemento
			 * di "listaGiocatori" (che ha indice "listaGiocatori.length-1"). 
			 */
			if (indiceGiocatore != listaGiocatori.size()-1) { 
				indiceGiocatore++;
			}
			else {
				indiceGiocatore = 0;
			}
			if (indiceGiocatore == this.primoGiocatore) { // "j" è stato già incrementato con l'istruzione precedente. Qui punta al prossimo giocatore
				turniGiocati++;
				//riga seguente da eliminare
				System.out.println("G: " + listaGiocatori.get(indiceGiocatore).personaggio() + "\tturno: " + turniGiocati);
			}
			
		}
		while (indiceGiocatore != this.primoGiocatore);   // quando "j" indica nuovamente "primoGiocatore" vuol dire che il turno è finito
	}
	
	public int getTurniGiocati(){
		return this.turniGiocati;
	}
	
	public boolean scialuppaRaggiunta() {
		return umanoSuScialuppa;
	}

}
