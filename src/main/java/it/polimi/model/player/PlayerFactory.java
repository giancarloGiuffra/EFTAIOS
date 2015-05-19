package it.polimi.model.player;

import java.util.ArrayList;
import java.util.List;

import it.polimi.model.exceptions.*;

/**
 * Classe per generare i Giocatori.
 *
 */
public class PlayerFactory {
    
    /**
     * Costruttore
     */
    private PlayerFactory(){
        //fa niente
    }
    
    /**
     * Metodo per generare un giocatore per il personaggio di gioco
     * @param personaggio   personaggio da associare al giocatore
     * @return player   giocatore associato al personaggio
     */
    private static Player createPlayer(Personaggio personaggio){
        switch(personaggio.razza()){
            case HUMAN: 
                return new HumanPlayer(personaggio);
            case ALIEN: 
                return new AlienPlayer(personaggio);
            default: 
                throw new IllegalRaceException("Razza deve essere HUMAN o ALIEN");
        }
    }
    
    /**
     * 
     * @return il giocatore capitano
     */
    public static Player createPlayer(){
        return new HumanPlayer(Personaggio.CAPITANO);
    }
    
    /**
     * Metodo per generare una lista di giocatori della cardinalità indicata.
     * @param numero numero di giocatori
     */
    public static List<Player> createPlayers(int numero){
        if(numero > Personaggio.numeroPersonaggi()) throw new NumeroMassimoGiocatoriException(String.format("Il numero massimo di giocatori è %d", Personaggio.numeroPersonaggi()));
        List<Player> listaGiocatori = new ArrayList<Player>();
        for(Personaggio personaggio:Personaggio.values()){
            if(listaGiocatori.size()<numero) listaGiocatori.add(PlayerFactory.createPlayer(personaggio));
        }
        return listaGiocatori;
    }
    
}