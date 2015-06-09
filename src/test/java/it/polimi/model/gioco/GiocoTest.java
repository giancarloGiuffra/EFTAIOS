package it.polimi.model.gioco;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;

import it.polimi.model.exceptions.BadSectorException;
import it.polimi.model.player.AzioneGiocatore;
import it.polimi.model.player.Player;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


/**
 * 
 * NB: i test effettuati ipotizzano che il tabellone usato sia GALILEI
 */
public class GiocoTest {

    private Gioco gioco;
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    @Before
    public void inizializza(){
        gioco = new Gioco(8);
    }
    
    @Test
    public void testCopyConstructor() {
        Gioco copia = new Gioco(gioco);
        assertThat(copia, is(gioco));
        assertNotSame(copia, gioco);
        assertThat(copia.hashCode(), is(gioco.hashCode()));
    }
    
    @Test
    public void testMovePlayerBasic(){
        Player player = gioco.currentPlayer();
        String nomeSettore = "";
        if(player.isAlien())
            nomeSettore = "L04";
        else
            nomeSettore = "L09";
        gioco.moveCurrentPlayer(nomeSettore);
        assertThat(gioco.posizioni().get(player), is(gioco.tabellone().getSettore(nomeSettore)));
    }
    
    @Test
    public void testMovePlayerThrowsExceptionSettoreLontano(){
        exception.expect(BadSectorException.class);
        gioco.moveCurrentPlayer("W14");
    }
    
    @Test
    public void testMovePlayerThrowsExceptionTiDeviSpostare(){
        exception.expect(BadSectorException.class);
        gioco.moveCurrentPlayer(gioco.currentPlayerPosition());
    }
    
    @Test
    public void testMovePlayerThrowsExceptionNonEsisteSentieroValido(){
    	while(!gioco.currentPlayer().isAlien())
    		gioco.finishTurn();
    	exception.expect(BadSectorException.class);
    	gioco.moveCurrentPlayer("K08"); //esce dal while se il currentPlayer è alieno
    }
    
    @Test
    public void testGetValidActionsForPlayerAlien(){
    	while(!gioco.currentPlayer().isAlien())
    		gioco.finishTurn();
    	gioco.moveCurrentPlayer("M06");
    	assertThat(gioco.getValidActionsForCurrentPlayer(), containsInAnyOrder(AzioneGiocatore.ATTACCA,AzioneGiocatore.PESCA_CARTA));
    }
    
    @Test
    public void testGetValidActionsForPlayerHuman(){
    	while(!gioco.currentPlayer().isHuman())
    		gioco.finishTurn();
    	gioco.moveCurrentPlayer("L09");
    	assertThat(gioco.getValidActionsForCurrentPlayer().isEmpty(), is(true));
    }

}
