package it.polimi.model.gioco;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import it.polimi.model.exceptions.BadSectorException;
import it.polimi.model.player.Player;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

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

}
