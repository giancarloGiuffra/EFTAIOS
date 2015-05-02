package it.polimi.model.player;

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
     * Metodo per controllare se la mossa è valida
     * @param from  settore di partenza
     * @param to    settore di arrivo
     * @return true se la mossa è valida, false altrimenti
     */
    @Override
    public boolean isMoveValid(Settore from, Settore to) {
        return to.isValidDestinationForAlien() && to.isAtMostTwoSectorAway(from);
    }

}
