package it.polimi.model.player;

import it.polimi.model.exceptions.*;

public class PlayeFactory {
    
    public Player createPlayer(Personaggio personaggio){
        switch(personaggio.razza()){
            case HUMAN: 
                return new HumanPlayer(personaggio);
            case ALIEN: 
                return new AlienPlayer(personaggio);
            default: 
                throw new IllegalRaceException("Razza deve essere HUMAN o ALIEN");
        }
    }
    
}
