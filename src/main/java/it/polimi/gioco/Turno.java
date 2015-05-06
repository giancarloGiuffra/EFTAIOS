package it.polimi.gioco;

import it.polimi.model.player.Personaggio;
import java.util.Random;

public class Turno {
	
	private int primoGiocatore;
	private Personaggio listaGiocatori[];
	private int turniGiocati = 0;
	
	/* All'inizio di una partita, quando viene inizializzato per la prima volta un oggetto di 
	 * tipo "Turno", viene scelto casualmente il giocatore da cui comincerà il giro
	 */ 
	public Turno(int numeroGiocatori, Personaggio listaGiocatori[]) {
		Random random = new Random();
		this.primoGiocatore = random.nextInt(numeroGiocatori); 
		this.listaGiocatori = listaGiocatori;
	}
	
	/* "svolgimentoTurno" non fa altro che simulare un turno di gioco eseguendo un ciclo
	 * per "i" volte (i = numero dei giocatori in gioco). La variabile "j" indicizza il 
	 * giocatore che sta giocando il turno corrente. Essa viene aggiornata con la stessa
	 * frequenza di "i", ma i suoi valori saranno "traslati" rispetto a quest'ultima (in quanto
	 * il giocatore che comincia il turno è scelto in maniera casuale e quindi, in caso 
	 * si raggiunga l'ultimo elemento della lista prima che tutti i giocatori abbiano 
	 * preso parte al turno, si deve ripartire dal primo elemento)
	 */
	
	public void svolgimentoTurno(){  // aggiornare listaGiocatori in caso un giocatore muoia
		int j = this.primoGiocatore; //j = indice giocatore, i serve solo a scorrere il turno
		for (int i = 0; i < listaGiocatori.length; i++){
			j += i;
			//listaGiocatori[j].muove();
			if (j == primoGiocatore) {
				turniGiocati++;
				System.out.println("G: " + listaGiocatori[j] + "\tturno: " + turniGiocati);  //riga da eliminare
			}
			
		}
	}
	
	public int getTurniGiocati(){
		return this.turniGiocati;
	}

}
