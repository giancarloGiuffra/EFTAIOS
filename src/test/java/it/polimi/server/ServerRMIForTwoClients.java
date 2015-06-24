package it.polimi.server;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerRMIForTwoClients implements Runnable {

	GameServer server;
    private static final Logger LOGGER = Logger.getLogger(ServerRMIForTwoClients.class.getName());

    
    /**
     * Costruttore
     * @param client2Thread 
     * @param client1Thread 
     */
    public ServerRMIForTwoClients(){
         server = new GameServer(65510,65533,1); //così currentGameRoom non può che essere l'unica sala aperta
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
        server.startServerRMIOnlyForTest(2);
    }

    /**
     * getter per la game room
     * @return
     */
    public GameRoom gameRoom() {
		return this.server.currentGameRoom();
	}
}
