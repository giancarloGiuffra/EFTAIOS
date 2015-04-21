package it.deib.polimi.www.Matricola833112;

import java.util.Random;

public class Settore {
	
	public final int numeroRighe = 14;
	public final int numeroColonne = 25;
	public static int numeroScialuppe = 4;
	public static int numeroBasi = 2;
	public static int numeroVuoti;  // casuale
	public static int numeroSicuri; // casuale
	public static int numeroPericolosi; // totale 350
	public enum Tipi {VUOTO, SICURO, PERICOLOSO, SCIALUPPA, BASE};
	public final int riga;
	public final char colonna;
	public Tipi tipo;
	/* generatoreTipo(){
	 * 	controllo (numeroTipo > 0) {
	 		public Random generatoreTipo = new Random(Tipi.length); meno tipi già implementati
	 		numeroTipo--;
	 	}
	 */
	
	public Settore(char col, int row, Tipi tipo){
		colonna = col;
		riga = row;
		this.tipo = tipo;
	}
	
}
