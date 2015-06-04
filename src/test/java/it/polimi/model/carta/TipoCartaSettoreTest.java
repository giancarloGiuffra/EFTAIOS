package it.polimi.model.carta;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

public class TipoCartaSettoreTest {

	@Test
	public void testNome() {
		assertThat(TipoCartaSettore.RUMORE_MIO.nome(), is("RUMORE NEL TUO SETTORE"));
		assertThat(TipoCartaSettore.RUMORE_QUALUNQUE.nome(), is("RUMORE IN QUALUNQUE SETTORE"));
		assertThat(TipoCartaSettore.SILENZIO.nome(), is("SILENZIO"));
	}

}
