package it.polimi.model.player;

import it.polimi.model.sector.Settore;

public class HumanPlayer extends Player {

    HumanPlayer(Personaggio personaggio){super(personaggio);}
    
    public boolean isMoveValid(Settore from, Settore to) {
        return to.isValidDestinationForHuman() && to.isOneSectorAway(from);
    }

}
