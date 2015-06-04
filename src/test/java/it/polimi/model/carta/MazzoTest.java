package it.polimi.model.carta;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;

import org.junit.Test;

public class MazzoTest {

	private Mazzo mazzo = Mazzo.creaNuovoMazzoCarteSettore();
	
	@Test
	public void testCopyConstructor() {
		Mazzo copia = new Mazzo(mazzo);
		assertThat(mazzo, is(copia));
		assertNotSame(mazzo, copia);
		assertThat(mazzo.hashCode(), is(copia.hashCode()));
	}
	
	@Test
	public void testRimischia(){
		List<Carta> carte = Mazzo.buildCopyOfCarte(mazzo);
		Integer size = this.mazzo.size();
		this.mazzo.rimischia();
		assertThat(size, is(this.mazzo.size()));
		assertThat(mazzo.carte(), containsInAnyOrder(carte.toArray()) );
	}
	

}
