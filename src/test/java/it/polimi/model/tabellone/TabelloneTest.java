package it.polimi.model.tabellone;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import it.polimi.model.exceptions.BadSectorException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TabelloneTest {

	private Tabellone tabellone = TabelloneFactory.createTabellone("GALILEI");
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Test
	public void testCopyConstructor() {
		Tabellone copia = new Tabellone(this.tabellone);
		assertThat(tabellone, is(copia));
		assertNotSame(copia, tabellone);
	}
	
	@Test
	public void testEsisteSentieroValido(){
		assertThat(tabellone.esisteSentieroValido(tabellone.getSettore("N08"), tabellone.getSettore("O07")),is(true));
		assertThat(tabellone.esisteSentieroValido(tabellone.getSettore("L12"), tabellone.getSettore("P10")),is(true));
	}
	
	@Test
	public void testEsisteSentieroValidoThrowsException(){
		exception.expect(BadSectorException.class);
		tabellone.esisteSentieroValido(tabellone.getSettore("M06"), tabellone.getSettore("M08"));
	}

}
