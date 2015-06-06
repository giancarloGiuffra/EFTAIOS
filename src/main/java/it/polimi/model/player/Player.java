package it.polimi.model.player;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import it.polimi.common.logger.FilterAllLogs;
import it.polimi.common.logger.FilterHigherThanInfoLevelLogs;
import it.polimi.model.carta.Carta;
import it.polimi.model.carta.Mazzo;
import it.polimi.model.exceptions.InvalidSectorForAnnouncement;
import it.polimi.model.sector.Settore;

/**
 * Classe Astratta da cui derivare i giocatori.
 *
 */
public abstract class Player {
    
    private final Personaggio personaggio;
    private Mazzo mazzo;
    private static final Logger LOGGER = Logger.getLogger(Player.class.getName());
    
    //static block
    static{
        LOGGER.setFilter(new FilterAllLogs());
    }
    
    /**
     * Copy Constructor
     * @param source
     */
    public Player(Player source){
    	this.personaggio = source.personaggio;
    	this.mazzo = new Mazzo(source.mazzo);
    }
    
    /**
     * Costruttore
     * @param personaggio
     */
    Player(Personaggio personaggio){
        this.personaggio = personaggio;
        this.mazzo = new Mazzo();
    }
    
    /**
     *  Metodo per controllare se la mossa è valida
     * @param from  settore di partenza
     * @param to    settore di arrivo
     * @return true se la mossa è valida
     * @throws BadSectorException
     */
     public abstract boolean isMoveValid(Settore from, Settore to);

	/**
	 * Restituisce il personaggio del giocatore
	 * @return personaggio
	 */
    public Personaggio personaggio() {
		return personaggio;
	}
    
    public String nome(){
        return new StringBuilder().
                append(this.personaggio.nome()).
                append(" (").
                append(this.personaggio.toString()).
                append(")").toString();
    }
    
    /**
     * @return true se il personaggio del giocatore è alieno 
     */
    public boolean isAlien(){
        return this.personaggio.isAlien();
    }
    
    /**
     * @return true se il personaggio del giocatore è umano
     */
    public boolean isHuman(){
        return this.personaggio.isHuman();
    }
    
    /**
     * @return razza del personaggio del giocatore
     */
    public Razza razza(){
        return this.personaggio.razza();
    }

	/**
	 * Metodo per chiedere un settore al giocatore e annunciarlo
	 */
    public void annunciaSettore(Settore settore){
		if(!settore.isValidSectorForAnnouncement()) throw new InvalidSectorForAnnouncement("Non si può dichiarare rumore in questo settore");
		LOGGER.log(Level.INFO,String.format("%s dichiara RUMORE IN SETTORE [%s,%d]", this.nome(), settore.getColonna(), settore.getRiga()));
	}

	/**
	 * Metodo per dichiarare silenzio
	 */
    public void dichiaraSilenzio() {
        LOGGER.log(Level.INFO, String.format("%s dichiara SILENZIO", this.nome()));
	}
	
    /**
	 * Metodo per dichiarare di essere morto
	 */
    public void muore(){
        LOGGER.log(Level.INFO, String.format("%s è morto",this.nome()));
	}
    
    /**
     * Metodo per attaccare
     */
    public void attacca(Settore settore){
    	LOGGER.log(Level.INFO, String.format("%s dichiara ATTACCO IN SETTORE [%s,%d]", this.nome(), settore.getColonna(), settore.getRiga()));
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

	@Override
	public int hashCode() {
		return new HashCodeBuilder(233, 17).
				append(this.personaggio).
				append(this.mazzo).
				toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Player))
			return false;
		Player other = (Player) obj;
		return new EqualsBuilder().
				append(this.personaggio,other.personaggio).
				append(this.mazzo, other.mazzo).
				isEquals();
	}

	public Mazzo mazzo() {
		return this.mazzo;
	}
}
