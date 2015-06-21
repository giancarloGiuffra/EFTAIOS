package it.polimi.server;

import it.polimi.client.SocketInterface;
import it.polimi.common.observer.BaseObservable;
import it.polimi.common.observer.Event;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerSocketForTwoCLients extends BaseObservable implements Runnable {
    
    GameServer server;
    private static final Logger LOGGER = Logger.getLogger(ServerSocketForTwoCLients.class.getName());

    
    /**
     * Costruttore
     * @param client2Thread 
     * @param client1Thread 
     */
    public ServerSocketForTwoCLients(){
         server = new GameServer(65535,65510,1); //così currentGameRoom non può che essere l'unica sala aperta
    }
    
    /**
     * chiude il server
     */
    public void close(){
        server.close();
    }
    
    /**
     * fa partire il server, gestisce le due connessioni, inizializza la sal e aspetta i thread dei client
     */
    @Override
    public void run(){
        try {
            server.startServerSocketOnlyForTest(2);
        } catch (IOException e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
        }
    }

    /**
     * getter per la game room
     * @return
     */
    public GameRoom gameRoom() {
		return this.server.currentGameRoom();
	}

}
