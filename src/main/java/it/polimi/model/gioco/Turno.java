package it.polimi.model.gioco;

import it.polimi.model.player.Personaggio;
import java.util.Random;

public class Turno {
    
    private int numeroGiocatore;
    private Personaggio listaGiocatori[];
    private boolean terminePartita = false;
    
    /* All'inizio di una partita, quando viene inizializzato per la prima volta un oggetto di 
     * tipo "Turno", viene scelto casualmente il giocatore da cui comincerà il giro
     */ 
    public Turno(int numeroGiocatori, Personaggio listaGiocatori[]) {
        Random random = new Random();
        this.numeroGiocatore = random.nextInt(numeroGiocatori); 
        this.listaGiocatori = listaGiocatori;
    }
    
    /* il gioco continuerà fino a quando la variabile booleana "terminePartita" 
     * non assumerà il valore "true". Quindi questa variabile costituisce l'unica 
     * condizione che determina l'uscita dal ciclo (il contatore "i" viene riportato a 
     * 0 ogni volta che viene raggiunta la coda dell'array, in modo da simulare la
     * ripetizione del giro)
     */
    public void giroTurni() { 
        /* metodo da migliorare: contare il numero di turni giocati da ciascun giocatore,
         * in modo da verificare la condizione di fine partita dopo 39 turni.
         * Gestire anche le altre condizioni di fine partita
         */
        for (int i = this.numeroGiocatore; i < listaGiocatori.length && terminePartita == false; i++) {
            listaGiocatori[i].muove();  // implementare il metodo 'muove' per ogni personaggio
            if (i == listaGiocatori.length-1) {
                i = 0;
            }
        }
    }

}