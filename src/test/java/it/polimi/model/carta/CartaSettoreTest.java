package it.polimi.model.carta;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;

import org.junit.Test;

public class CartaSettoreTest {

	private List<Carta> lista = CartaSettore.getListCarteSettoreDiTipo(TipoCartaSettore.SILENZIO, 1);
	
	@Test
	public void testCopyConstructor() {
		CartaSettore carta = (CartaSettore) lista.get(0);
		CartaSettore cartaCopia = new CartaSettore(carta);
		assertThat(carta.tipo(), is(cartaCopia.tipo()));
		assertThat(carta.hasIconaOgetto(), is(cartaCopia.hasIconaOgetto()));
		assertNotSame(carta, cartaCopia);
	}

}
