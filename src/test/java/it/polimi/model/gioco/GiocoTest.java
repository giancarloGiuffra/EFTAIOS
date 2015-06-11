package it.polimi.model.gioco;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;

import it.polimi.model.carta.Carta;
import it.polimi.model.exceptions.BadSectorException;
import it.polimi.model.exceptions.BadSectorPositionNameException;
import it.polimi.model.exceptions.InvalidSectorForAnnouncement;
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
    
    @Test
    public void testCurrentPlayerPescaCartaSettore(){
    	gioco.currentPlayerPescaCartaSettore();
    	assertThat(gioco.currentPlayer().mazzo().size(), is(1));
    }
    
    @Test
    public void testCalcolaSettoriValidiForCurrentPlayerHuman(){
    	while(!gioco.currentPlayer().isHuman())
    		gioco.finishTurn();
    	assertThat(gioco.calcolaSettoriValidiForCurrentPlayer().size(), is(5));
    	assertThat(gioco.calcolaSettoriValidiForCurrentPlayer(), containsInAnyOrder("K08","K09","L09","M09","M08"));
    }
    
    @Test
    public void testCalcolaSettoriValidiForCurrentPlayerAlien(){
    	while(!gioco.currentPlayer().isAlien())
    		gioco.finishTurn();
    	assertThat(gioco.calcolaSettoriValidiForCurrentPlayer().size(), is(10));
    	assertThat(gioco.calcolaSettoriValidiForCurrentPlayer(), containsInAnyOrder("J06","J05","K06","K05","L04","L05","M05","M06","N05","N06"));
    }
    
    @Test
    public void testCurrentPlayerAnnunciaSettoreThrowsInvalidcurrentPlayerAnnunciaSettore(){
    	exception.expect(InvalidSectorForAnnouncement.class);
    	gioco.currentPlayerAnnunciaSettore("L06");
    }
    
    @Test
    public void testCurrentPlayerAnnunciaSettoreThrowsBadSectorPositionNameException(){
    	exception.expect(BadSectorPositionNameException.class);
    	gioco.currentPlayerAnnunciaSettore("asfasf");
    }
    
    @Test
    public void testIsThisLastPlayerDisconnecting(){
    	assertThat(gioco.isThisLastPlayerDisconnecting(), is(false));
    }
    
}
