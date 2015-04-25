package it.polimi.model.player;

import it.polimi.model.sector.Settore;

abstract class Player {
    
    private final Personaggio personaggio;
    
    Player(Personaggio personaggio){this.personaggio=personaggio;}
    
    abstract public boolean isMoveValid(Settore from, Settore to);
}
