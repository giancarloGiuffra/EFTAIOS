package it.polimi.model.player;

import it.polimi.model.sector.Settore;

public class HumanPlayer extends Player {

    HumanPlayer(Personaggio personaggio){super(personaggio);}
    
    @Override
    public boolean isMoveValid(Settore from, Settore to) {
        return to.isValidDestinationForHuman() && to.isOneSectorAway(from);
    }

}
