// inserire "127.0.0.1" come indirizzo IP del server per eseguire il test
package it.polimi.gui;

import static org.junit.Assert.*;

import org.junit.Test;

import it.polimi.client.ClientGUI;
import it.polimi.client.TipoInterface;

public class GUISceltaInterfacciaTest {
	
	private ClientGUI clientGUI = new ClientGUI();
	private GUISceltaInterfaccia guiDiTest = clientGUI.getInterfaccia();
	
	@Test
	public void testComunicaTecnologiaDiComunicazione() {
		Pulsante inizioPartitaConRMI = new Pulsante("Start con Socket");
		guiDiTest.comunicaTecnologiaDiComunicazioneHelpTest(inizioPartitaConRMI.getNomePulsante());
		assertTrue(guiDiTest.getTipoInterfaccia().equals(TipoInterface.SOCKET_GUI));
	}

}
