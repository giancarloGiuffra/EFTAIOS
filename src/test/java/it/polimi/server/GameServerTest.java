package it.polimi.server;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.willAnswer;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.matches;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import it.polimi.client.NetworkInterfaceForClient;
import it.polimi.client.RMIInterface;
import it.polimi.client.SocketInterface;
import it.polimi.client.TipoInterface;
import it.polimi.common.observer.BaseObservable;
import it.polimi.common.observer.BaseObserver;
import it.polimi.common.observer.Event;
import it.polimi.common.observer.SetModelInGameServerTest;
import it.polimi.controller.Controller;
import it.polimi.main.Main;
import it.polimi.main.RispostaPerView;
import it.polimi.model.Model;
import it.polimi.model.player.AzioneGiocatore;
import it.polimi.view.PrintWriterPlus;
import it.polimi.view.View;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;

public class GameServerTest {
    
    private static final Logger LOGGER = Logger.getLogger(GameServerTest.class.getName());
    private static final Pattern CHIEDI_MOSSA = Pattern.compile("Indica la tua mossa:.*", Pattern.DOTALL); //DOTALL fa che . matchi anche i line terminator
    private static final Pattern MOSSA_NON_VALIDA = Pattern.compile("La mossa inserita non è valida.*");
    private static final Pattern BENVENUTO = Pattern.compile("Benvenuto.*");
	private static final Pattern TURNO = Pattern.compile("Tocca a te .*\\((?<personaggio>.*)\\) - Turno numero \\d{1,} - Posizione (?<posizione>.{3})");
    private static final Pattern TURNO_FINITO = Pattern.compile("Il tuo turno è finito.*", Pattern.DOTALL);
    private static final Pattern PESCA_CARTA = Pattern.compile("Devi pescare una Carta Settore.*");
    private static final Pattern SCEGLIE_AZIONE = Pattern.compile("Le azioni possibili sono.*");
    private static final Pattern ANNUNCIA_SETTORE = Pattern.compile(".*RUMORE IN QUALUNQUE SETTORE");
    private static final Pattern CHIEDE_IP = Pattern.compile("Inserisci l'Indirizzo IP del Server.*");
    private NetworkInterfaceForClient client1;
    private NetworkInterfaceForClient client2;
    private RispostaPerServer rispostaClient1;
    private RispostaPerServer rispostaClient2;
    
    public void inizializza(TipoInterface tipo){
    	switch(tipo){
	    	case SOCKET:
	    		client1 = spy(new SocketInterface(Risposta.risposta("127.0.0.1")));
	    		client2 = spy(new SocketInterface(Risposta.risposta("127.0.0.1")));
	    		break;
	    	case RMI:
	    		client1 = spy(new RMIInterface(Risposta.risposta("127.0.0.1")));
	    		client2 = spy(new RMIInterface(Risposta.risposta("127.0.0.1")));
	    		break;
	    	default:
	    		break;
    	}
        rispostaClient1 = new RispostaPerServer(client1);
        rispostaClient2 = new RispostaPerServer(client2);

    }
    
    @Test
    public void testServerSocketForTwoClientsConnectionAndGameDynamics(){
        
        //inizializza client e risposte
    	inizializza(TipoInterface.SOCKET);
    	
    	//set up e test connessione
        ServerSocketForTwoClients server = new ServerSocketForTwoClients();
        Thread serverThread = new Thread(server);
        serverThread.start();
        client1.connectToServer();
        client2.connectToServer();
        try {
            serverThread.join(); //per aspettare il set up della game room
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Interrupted exception in join", e);
        }
        rispostaClient1.setModel(server.gameRoom().model());
        rispostaClient2.setModel(server.gameRoom().model());

        //given - behavior client1
        willAnswer(rispostaClient1.risposta("mossa_aleatoria")).given(client1).print(matches(TURNO.pattern()));
        willAnswer(rispostaClient1.risposta(System.lineSeparator())).given(client1).print(matches(PESCA_CARTA.pattern()));
        willAnswer(rispostaClient1.risposta("1")).given(client1).print(matches(SCEGLIE_AZIONE.pattern()));
        willAnswer(rispostaClient1.risposta("announce: M09")).given(client1).print(matches(ANNUNCIA_SETTORE.pattern()));
        
        //given - behavior client2
        willAnswer(rispostaClient2.risposta("mossa_aleatoria")).given(client2).print(matches(TURNO.pattern()));
        willAnswer(rispostaClient2.risposta(System.lineSeparator())).given(client2).print(matches(PESCA_CARTA.pattern()));
        willAnswer(rispostaClient2.risposta("1")).given(client2).print(matches(SCEGLIE_AZIONE.pattern()));
        willAnswer(rispostaClient2.risposta("announce: M09")).given(client2).print(matches(ANNUNCIA_SETTORE.pattern()));
        
        // when
        server.gameRoom().run();
        Thread client1Thread = new Thread(client1);
        Thread client2Thread = new Thread(client2);
        client1Thread.start();
        client2Thread.start();
        try {
            client1Thread.join();
            client2Thread.join();
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Interrupted exception in join", e);
        }
        
        //chiusura server
        server.close();

    }
    
    @Test
    public void testRMIServerForTwoClientsConnectionOnly(){
    	
    	//inizializza client e risposte
    	inizializza(TipoInterface.RMI);
    	
    	//set up 
    	ServerRMIForTwoClients server = new ServerRMIForTwoClients();
    	server.run();
    	
        
        //connessioni
        client1.connectToServer();
        client2.connectToServer();
        
    }

}
