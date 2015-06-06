package it.polimi.model.gioco;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import it.polimi.model.player.Player;
import it.polimi.model.player.PlayerFactory;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class TurnoTest{

	private Turno turno;
	private List<Player> lista;
	
	@Before
	public void initLista(){
		lista = PlayerFactory.createPlayers(3);
		turno = new Turno(lista);
	}

	@Test
	public void testFinishTurn() {
		for(Player player : lista){
		assertThat(turno.currentPlayer(),is(player));
		turno.finishTurn();
		}
	}

	@Test
	public void testTurnsOver() {
		for(int i = 0; i < turno.numeroMassimoDiTurni()*turno.players().size(); i++){
			turno.finishTurn();
		}
		assertThat(turno.turnsOver(), equalTo(true));
	}

	@Test
	public void testCurrentTurnAfterOneRound() {
		for(Player player : new ArrayList<Player>(turno.players())) turno.finishTurn();
		assertThat(turno.currentTurn(),equalTo(2));
	}


	@Test
	public void testRemove() {
		turno.remove(lista.get(0));
		assertTrue(turno.currentPlayer().equals(lista.get(1)));	
	}
	
	@Test
	public void testGetNextFirstPlayer(){
		for(Player player : lista){
			assertThat(turno.getNextFirstPlayer(), is(lista.get(1)));
		}
	}
	
	@Test
	public void testCopyConstructor(){
	    Turno copia = new Turno(turno);
	    assertThat(copia, is(turno));
	    assertNotSame(copia,turno);
	}

}
