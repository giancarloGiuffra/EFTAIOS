package it.polimi.server;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerRMIForTwoClients implements Runnable{
    
    GameServer server;
    private static final Logger LOGGER = Logger.getLogger(ServerRMIForTwoClients.class.getName());

    
    /**
     * Costruttore
     */
    public ServerRMIForTwoClients(){
         server = new GameServer(65510,65533,1); //così currentGameRoom non può che essere l'unica sala aperta
    }
    
    /**
     * fa partire il server, gestisce le due connessioni, inizializza la sal e aspetta i thread dei client
     */
    @Override
    public void run(){
        server.startServerRMIOnlyForTest(2);
    }

}
