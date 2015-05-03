package it.polimi.model.player;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.model.carta.Carta;
import it.polimi.model.carta.Mazzo;
import it.polimi.model.exceptions.IllegalAzioneGiocatoreException;
import it.polimi.model.sector.Settore;

/**
 * Classe Astratta da cui derivare i giocatori.
 *
 */
abstract public class Player {
    
    protected final Personaggio personaggio;
    private Mazzo mazzo;
    private Settore settore;
    private static final Logger LOGGER = Logger.getLogger(Player.class.getName());
    
    /**
     * Costruttore
     * @param personaggio
     */
    Player(Personaggio personaggio){
        this.personaggio=personaggio;
    }
    
    /**
     *  Metodo per controllare se la mossa è valida
     * @param from  settore di partenza
     * @param to    settore di arrivo
     * @return true se la mossa è valida, false altrimenti
     */
    abstract public boolean isMoveValid(Settore from, Settore to);

	/**
	 * Restituisce il personaggio del giocatore
	 * @return personaggio
	 */
    public Personaggio personaggio() {
		return personaggio;
	}
    
    /**
     * @return razza del personaggio del giocatore
     */
    public Razza razza(){
        return this.personaggio.razza();
    }

	/**
	 * Metodo per annunciare il settore in cui si trova il giocatore
	 */
    public void annunciaSettoreMio() {
        LOGGER.log(Level.INFO, String.format("RUMORE IN SETTORE [%s,%d]",this.settore.getColonna(),this.settore.getRiga()) );
	}

	/**
	 * Metodo per chiedere un settore al giocatore e annunciarlo
	 */
    public void annunciaSettore() {
        LOGGER.log(Level.INFO,"Inserire il nome del Settore da annunciare: ");
		Scanner scanner = new Scanner(System.in);
		String nome = scanner.nextLine();
		scanner.close();
		Settore.checkIfValidSectorName(nome);
		LOGGER.log(Level.INFO,String.format("RUMORE IN SETTORE [%s,%d]", Settore.getColonnaFromName(nome),Settore.getRigaFromName(nome)));
	}

	/**
	 * Metodo per dichiarare silenzio
	 */
    public void dichiaraSilenzio() {
        LOGGER.log(Level.INFO, "SILENZIO");
	}
	
	/**
	 * Metodo per usare una carta
	 * @param carta
	 */
    public void usaCarta(Carta carta){
		//carta.effetto(this); -->vecchia implementazione
        this.azione(carta);
	}
	
    /**
     * Chiama l'azione che il giocatore deve compiere all'usare la carta
     * @param carta
     */
	private void azione(Carta carta) {
        switch(carta.azione()){
            case ANNUNCIA_SETTORE:
                this.annunciaSettore();
                break;
            case ANNUNCIA_SETTORE_MIO:
                this.annunciaSettoreMio();
                break;
            case DICHIARA_SILENZIO:
                this.dichiaraSilenzio();
                break;
            default:
                throw new IllegalAzioneGiocatoreException("Azione Giocatore non valida");
        }
        
    }

    /**
	 * Metodo per dichiarare di essere morto
	 */
    public void muore(){
        LOGGER.log(Level.INFO, String.format("%s è morto",this.personaggio.nome()));
	}
	
	/**
	 * Metodo per pescare una carta dal mazzo
	 * @param mazzo mazzo da cui pescare la carta
	 * @return carta pescata
	 */
    public Carta pescaCarta(Mazzo mazzoDaCuiPescare){
		return mazzoDaCuiPescare.getCarta();
	}
    
    /**
     * Salva carta nel mazzo del giocatore
     * @param carta da salvare
     */
    public void salvaCarta(Carta carta){
        this.mazzo.putCarta(carta);
    }
}
