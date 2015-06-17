package it.polimi.gui;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import it.polimi.client.SocketGUIInterface;
import it.polimi.model.sector.Settore;
import it.polimi.model.sector.TipoSettore;
import it.polimi.model.tabellone.Tabellone;
import it.polimi.model.tabellone.TabelloneFactory;

public class GUITest {
	
	private SocketGUIInterface socketGUIInterface;
	private GUI guiDiTest = new GUI(socketGUIInterface);
	private List<String> settori = new ArrayList<String>();
	private ArrayList<Pulsante> listaPulsantiSettore = guiDiTest.getListaPulsantiSettore();
	private static Tabellone galilei = TabelloneFactory.createTabellone("GALILEI");
	
	@Test
	public void testSetColore() {
		Pulsante L04 = new Pulsante("L04");
		guiDiTest.setColorePulsante("Sicuri", L04.getButton());
		assertFalse(L04.getButton().getBackground().equals(Color.BLACK));
		assertTrue(L04.getButton().isVisible());
		assertTrue(L04.getButton().getBackground().equals(Color.WHITE));
	}
	
	@Test
	public void testAbilitaSettoriAdiacenti() {
		Settore K06 = galilei.getSettore("K06");
		Settore L05 = galilei.getSettore("L05");
		settori.add(K06.getNome());
		settori.add(L05.getNome());
		guiDiTest.abilitaSettoriAdiacenti(settori);
		//TODO
	}
	
	public void testEvidenziaSpostamento() {
		//TODO
	}
	
	public void testGetIndiceAzione() {
		//TODO
	}

}
