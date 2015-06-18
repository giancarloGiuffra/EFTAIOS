package it.polimi.server;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerSocketForTwoCLients implements Runnable {
    
    GameServer server;
    private static final Logger LOGGER = Logger.getLogger(ServerSocketForTwoCLients.class.getName());

    
    /**
     * Costruttore
     */
    public ServerSocketForTwoCLients(){
         server = new GameServer(65535,65510);
    }
    
    /**
     * chiude il server
     */
    public void close(){
        server.close();
    }
    
    @Override
    public void run(){
        try {
            server.startServerSocketOnlyForTest(2);
        } catch (IOException e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
        }
    }
}
