package it.polimi.gui;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import it.polimi.client.SocketGUIInterface;
import it.polimi.model.sector.Settore;
import it.polimi.model.tabellone.Tabellone;
import it.polimi.model.tabellone.TabelloneFactory;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class GUITest {
	
	private SocketGUIInterface socketGUIInterface = new SocketGUIInterface();
	private GUI guiDiTest = socketGUIInterface.getGUI();
	private ArrayList<Pulsante> listaPulsantiSettore;
	private List<String> settori = new ArrayList<String>();
	private static Tabellone galilei = TabelloneFactory.createTabellone("GALILEI");
	
	@Before
	public void operazioniPreliminari() {
		guiDiTest.creaListaPulsantiSettoreHelpTest();
		ArrayList<Pulsante> listaPulsantiSettore = guiDiTest.getListaPulsantiSettore();
		this.listaPulsantiSettore = listaPulsantiSettore;
	}
	
	@Test
	public void testSetColore() {
		Pulsante l04 = new Pulsante("L04");
		guiDiTest.setColorePulsanteHelpTest("Sicuri", l04.getButton());
		assertFalse(l04.getButton().getBackground().equals(Color.BLACK));
		assertTrue(l04.getButton().isVisible());
		assertTrue(l04.getButton().getBackground().equals(Color.WHITE));
	}
	
	@Test
	public void testAbilitaSettoriAdiacenti() {
		Settore k06 = galilei.getSettore("K06");
		Settore l05 = galilei.getSettore("L05");
		settori.add(k06.getNome());
		settori.add(l05.getNome());
		guiDiTest.abilitaSettoriAdiacenti(settori);
		for (String s : settori) {
			for (Pulsante p : listaPulsantiSettore) {
				if (p.getNomePulsante().equals(s)) {
					assertTrue(p.getButton().isEnabled());
				}
			}
		}
	}
	
	@Test
	public void testEvidenziaSpostamento() {
		Pulsante l04 = new Pulsante("L04");
		guiDiTest.evidenziaSpostamentoHelpTest(l04);
		assertFalse(l04.getButton().getBackground().equals(Color.WHITE));
		assertTrue(l04.getButton().getBackground().equals(Color.GREEN));
	}
	
	@Test
	public void testGetIndiceAzione() {
		List<String> azioniPossibili = new ArrayList<String>();
		azioniPossibili.add("ATTACCA");
		azioniPossibili.add("PESCA_CARTA");
		assertThat(guiDiTest.getIndiceAzioneHelpTest(azioniPossibili, "PESCA_CARTA"), is("2"));
	}
	
	@Test
	public void testRegistraEAnnunciaSpostamento() {
		Pulsante m08 = new Pulsante("M08");
		String inputAnnunciato;
		guiDiTest.registraSpostamentoHelpTest(m08);
		assertTrue(guiDiTest.isInputInserito());
		inputAnnunciato = guiDiTest.annunciaInput();
		assertFalse(guiDiTest.isInputInserito());
		assertThat(inputAnnunciato, is("move to: M08"));
	}

}
