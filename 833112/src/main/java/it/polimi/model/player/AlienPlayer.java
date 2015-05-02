package it.polimi.model.player;

import it.polimi.model.sector.Settore;

public class AlienPlayer extends Player {

    AlienPlayer(Personaggio personaggio){super(personaggio);}
    
    @Override
    public boolean isMoveValid(Settore from, Settore to) {
        return to.isValidDestinationForAlien() && to.isAtMostTwoSectorAway(from);
    }

}
