package it.polimi.model.sector;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import it.polimi.model.exceptions.BadSectorPositionNameException;
import it.polimi.model.tabellone.Tabellone;
import it.polimi.model.tabellone.TabelloneFactory;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class SettoreTest {
	
	private Tabellone tabellone = TabelloneFactory.createTabellone("GALILEI");
	private List<String> A01 = Arrays.asList("A02","B01");
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Test
	public void testBasico(){
		Settore settore = new Settore('A',3,TipoSettore.INACCESSIBILE);
		assertThat(settore.getNome(), is("A03"));
		assertThat(settore.getColonna(), is('A'));
		assertThat(settore.getRiga(), is(3));
	}
	
	@Test
	public void testGetSettoriAdiacenti() {
		assertThat(this.tabellone.getSettore("A01").getSettoriAdiacenti(), is(this.A01));
	}
	
	@Test
	public void testCopyConstructor(){
		Settore L09 = this.tabellone.getSettore("L09");
		Settore L09Copy = new Settore(L09);
		assertThat(L09,is(L09Copy));
		assertNotSame(L09,L09Copy);
		assertThat(L09.hashCode(), is(L09Copy.hashCode()));
	}
	
	@Test
	public void testGetSettoriAdiacentiADistanzaDue(){
		assertThat(this.tabellone.getSettore("A01").getSettoriAdiacentiADistanzaDue(), hasSize(6));
		assertThat(this.tabellone.getSettore("A01").getSettoriAdiacentiADistanzaDue(),containsInAnyOrder("A02","A03","B01","B02","C02","C01"));
	}
	
	@Test
	public void testCheckIfValidSectorNameThrowsException(){
		exception.expect(BadSectorPositionNameException.class);
		Settore.checkIfValidSectorName("ewgasdgsdafg");
	}

}
