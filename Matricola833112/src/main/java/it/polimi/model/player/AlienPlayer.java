package it.polimi.model.player;

import it.polimi.model.sector.Settore;

public class AlienPlayer extends Player {

    AlienPlayer(Personaggio personaggio){super(personaggio);}
    
    public boolean isMoveValid(Settore from, Settore to) {
        return to.isValidDestinationForAlien() && to.isTwoSectorAway(from);
    }

}
