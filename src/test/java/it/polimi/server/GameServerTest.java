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
import it.polimi.client.SocketInterface;
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

public class GameServerTest implements BaseObserver{
    
    private static final Logger LOGGER = Logger.getLogger(GameServerTest.class.getName());
    private static final Pattern CHIEDI_MOSSA = Pattern.compile("Indica la tua mossa:.*", Pattern.DOTALL); //DOTALL fa che . matchi anche i line terminator
    private static final Pattern MOSSA_NON_VALIDA = Pattern.compile("La mossa inserita non è valida.*");
    private static final Pattern BENVENUTO = Pattern.compile("Benvenuto.*");
    private static final Pattern TURNO = Pattern.compile("Tocca a .* Posizione (?<posizione>.{3})");
    private static final Pattern TURNO_FINITO = Pattern.compile("Il tuo turno è finito.*", Pattern.DOTALL);
    private static final Pattern PESCA_CARTA = Pattern.compile("Devi pescare una Carta Settore.*");
    private static final Pattern SCEGLIE_AZIONE = Pattern.compile("Le azioni possibili sono.*");
    private static final Pattern ANNUNCIA_SETTORE = Pattern.compile(".*RUMORE IN QUALUNQUE SETTORE");
    private SocketInterface client1;
    private SocketInterface client2;
    private RispostaPerServer rispostaClient1;
    private RispostaPerServer rispostaClient2;
    
    @Before
    public void inizializza(){
        client1 = spy(new SocketInterface());
        client2 = spy(new SocketInterface());
        rispostaClient1 = new RispostaPerServer(client1);
        rispostaClient2 = new RispostaPerServer(client2);

    }
    
    /*@Test
    public void testServerSocketForTwoCLients(){
        
        //set up
    	Thread client1Thread = new Thread(client1);
        Thread client2Thread = new Thread(client2);
        ServerSocketForTwoCLients server = new ServerSocketForTwoCLients(client1Thread,client2Thread);
        this.rispostaClient1.setGameServerTest(this);
        this.rispostaClient2.setGameServerTest(this);
        server.addObserver(this); //per inviare l'aggiornamento del model per le risposte che dipendono dala posizione del client
        Thread serverThread = new Thread(server);
        serverThread.start();
        client1.connectToServer();
        client1Thread.start();
        client2.connectToServer(); 
        client2Thread.start(); // dovrei avere serverThread che aspetta nel join con i due client trhead che aspettano le comunicazioni dal server
    	
    	//given - behavior client1
        //willAnswer(rispostaClient1.risposta(System.lineSeparator())).given(client1).print(matches(BENVENUTO.pattern()));
        willAnswer(rispostaClient1.risposta("mossa_aleatoria")).given(client1).print(matches(TURNO.pattern()));
        willAnswer(rispostaClient1.risposta(System.lineSeparator())).given(client1).print(matches(PESCA_CARTA.pattern()));
        willAnswer(rispostaClient1.risposta("1")).given(client1).print(matches(SCEGLIE_AZIONE.pattern()));
        willAnswer(rispostaClient1.risposta("announce: M09")).given(client1).print(matches(ANNUNCIA_SETTORE.pattern()));
        
      //given - behavior client2
        //willAnswer(rispostaClient2.risposta(System.lineSeparator())).given(client2).print(matches(BENVENUTO.pattern()));
        willAnswer(rispostaClient2.risposta("mossa_aleatoria")).given(client2).print(matches(TURNO.pattern()));
        willAnswer(rispostaClient2.risposta(System.lineSeparator())).given(client2).print(matches(PESCA_CARTA.pattern()));
        willAnswer(rispostaClient2.risposta("1")).given(client2).print(matches(SCEGLIE_AZIONE.pattern()));
        willAnswer(rispostaClient2.risposta("announce: M09")).given(client2).print(matches(ANNUNCIA_SETTORE.pattern()));

        //when
        (new Thread(server.gameRoom())).start();//faccio partire la game room
        try {
			serverThread.join();
		} catch (InterruptedException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		} //wait for server to finish
        server.close(); //chiude server , libera la porta
    }*/

	@Override
	public void notifyRicevuto(BaseObservable source, Event event) {
		if(source instanceof ServerSocketForTwoCLients){
			if(event.name().equals("SetModelInGameServerTest")){
				Model model = ((SetModelInGameServerTest)event).model();
				this.rispostaClient1.setModel(model);
				this.rispostaClient2.setModel(model);
				synchronized(this){
					this.notifyAll(); //neccessario per le risposte che stanno aspettando l'assegnamento del model
				}
			}
		}
		
	}


}
