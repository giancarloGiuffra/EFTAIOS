// Nota per l'interfaccia grafica: la cartina fornita non è più utilizzabile
package it.polimi.deib.www.Matricola833112;

import java.util.Random;

public class Settore {
	
	/* le costanti tengono conto del numero di righe e del numero di colonne della mappa,
	 * dei settori totali al suo interno, del numero di basi e scialuppe e del massimo 
	 * numero di settori che possono essere inizializzati come vuoti (valore 
	 * arbitrariamente fissato a 50)
	 */
	public final int numeroRighe = 14;
	public final int numeroColonne = 25;
	public final int settoriTotali = numeroRighe * numeroColonne;
	public final int vuotiTotali = 50;
	public final int basiTotali = 2;
	public final int scialuppeTotali = 4;
	/* le variabili statiche tengono conto del numero di settori ancora da inizializzare 
	 * per ognuno dei tipi possibili. Questo è necessario affinché il metodo "Random" non
	 * generi un numero eccessivo di scialuppe o di basi
	 */
	public static int scialuppeDaInizializzare = 4;
	public static int basiDaInizializzare = 2;
	public static int vuotiInizializzati = 0; 
	/* il numero di settori sicuri e di settori pericolosi che comporranno la mappa 
	 * non è determinato a priori, ma verrà stabilito in maniera casuale man mano
	 * che i settori verranno inizializzati
	 */
	public static int numeroSicuri; 
	public static int numeroPericolosi; 
	public enum Tipi {VUOTO, SICURO, PERICOLOSO, SCIALUPPA, BASE};
	//public final int riga;
	//public final char colonna;
	public Tipi tipologia;
	public Tipi tipoSettore(){  
		/* viene generato un intero casuale "indiceCasuale". Questo numero verrà utilizzato
		 * come indice per l'array contenente gli elementi dell'enum "Tipi".
		 */
		Random random = new Random();
		Tipi opzioniTipo[] = Tipi.values();
		int indiceCasuale = random.nextInt(opzioniTipo.length);
		switch(indiceCasuale){
		case 0:
			if (vuotiInizializzati < vuotiTotali){
				tipologia = opzioniTipo[indiceCasuale];
				vuotiInizializzati++;
				break;}
			else tipoSettore();
		case 1:
			if (numeroSicuri < settoriTotali - vuotiTotali - basiTotali - scialuppeTotali){
				/* nel caso limite tutti i settori disponibili per il movimento 
				 * saranno sicuri (nessun settore pericoloso)
				 */
				tipologia = opzioniTipo[indiceCasuale];
				numeroSicuri++;
				break;
				}
			else tipoSettore();
		case 2: 
			if (numeroPericolosi < settoriTotali - vuotiTotali - basiTotali - scialuppeTotali){
				// anche qui il caso limite è: tutti i settori "giocabili" pericolosi, nessuno sicuro
				tipologia = opzioniTipo[indiceCasuale];
				numeroPericolosi++;
				break;
				}
			else tipoSettore();
		case 3:
			if (scialuppeDaInizializzare > 0) {
				// viene controllato che le scialuppe non siano già state tutte inizializzate
				tipologia = opzioniTipo[indiceCasuale];
				scialuppeDaInizializzare--;
				break;
				}
			else tipoSettore();
		case 4:
			if (basiDaInizializzare > 0) {
				tipologia = opzioniTipo[indiceCasuale];
				basiDaInizializzare--;
				break;
			}
			else tipoSettore();
			}
		return tipologia;
		}
		
	
	/*public Settore(char col, int row, Tipi tipologia){
		colonna = col;
		riga = row;
		this.tipologia = tipo;
	}*/
	
}
