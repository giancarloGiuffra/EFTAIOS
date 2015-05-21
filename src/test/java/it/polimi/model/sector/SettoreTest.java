package it.polimi.model.sector;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;
import it.polimi.model.tabellone.Tabellone;
import it.polimi.model.tabellone.TabelloneFactory;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

public class SettoreTest extends TestCase{
	
	private Tabellone tabellone = TabelloneFactory.createTabellone("GALILEI");
	private List<String> A01 = Arrays.asList("A02","B01");
	
	@Test
	public void testGetSettoriAdiacenti() {
		assertThat(this.tabellone.getSettore("A01").getSettoriAdiacenti(), is(this.A01));
	}

}
