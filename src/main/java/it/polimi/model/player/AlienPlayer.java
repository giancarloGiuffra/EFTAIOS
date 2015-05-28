package it.polimi.model.player;

import it.polimi.model.exceptions.BadSectorException;
import it.polimi.model.exceptions.NotValidAlienPlayer;
import it.polimi.model.sector.Settore;

/**
 * Classe derivata da Player per rappresentare il giocatore alieno.
 * La razza dipende dal Personaggio.
 *
 */
public class AlienPlayer extends Player {

    /**
     * Costruttore
     * @param personaggio
     */
    AlienPlayer(Personaggio personaggio){
        super(personaggio);
    }
    
    /**
     * Copy Constructor
     * @param player
     */
    public AlienPlayer( Player player){
    	super(checkIfValidAlienPlayer(player));
    }
    
    private static Player checkIfValidAlienPlayer(Player player) {
		if(!player.razza().equals(Razza.ALIEN)) throw new NotValidAlienPlayer();
		return player;
	}

	/**
     * Metodo per controllare se la mossa è valida
     * @param from  settore di partenza
     * @param to    settore di arrivo
     * @return true se la mossa è valida, false altrimenti
     */
    @Override
    public boolean isMoveValid(Settore from, Settore to) {
        if(from.equals(to)) throw new BadSectorException("Ti devi spostare!");
        if(!to.isAtMostTwoSectorAway(from)) throw new BadSectorException(String.format("Il settore %s si trova a più di due settore di distanza da %s", to.getNome(),from.getNome()));
        return to.isValidDestinationForAlien() && to.isAtMostTwoSectorAway(from);
    }

}
