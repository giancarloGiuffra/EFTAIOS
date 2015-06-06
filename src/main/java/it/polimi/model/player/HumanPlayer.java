package it.polimi.model.player;

import it.polimi.model.exceptions.BadSectorException;
import it.polimi.model.exceptions.NotValidHumanPlayerException;
import it.polimi.model.sector.Settore;

/**
 * Classe derivata da Player per rappresentare il giocatore umano.
 * La razza dipende dal Personaggio.
 *
 */
public class HumanPlayer extends Player {

    /**
     * Costruttore
     * @param personaggio
     */
    HumanPlayer(Personaggio personaggio){
        super(personaggio);
    }
    
    /**
     * Copy Constructor
     * @param player
     */
    public HumanPlayer(Player player){
    	super(checkIfValidHumanPlayer(player));
    }
    
    private static Player checkIfValidHumanPlayer(Player player) {
		if(!player.razza().equals(Razza.HUMAN)) throw new NotValidHumanPlayerException();
		return player;
	}

	/**
     * Metodo per controllare se la mossa è valida
     * @param from  settore di partenza
     * @param to    settore di arrivo
     * @return true se la mossa è valida
     * @throws BadSectorException
     */
    @Override
    public boolean isMoveValid(Settore from, Settore to) {
        if(from.equals(to)) throw new BadSectorException("Ti devi spostare!");
        if(!to.isOneSectorAway(from)) throw new BadSectorException(String.format("Il settore %s è a più di un settore di distanza da %s", to.getNome(),from.getNome()));
        return to.isValidDestinationForHuman() && to.isOneSectorAway(from);
    }

}
