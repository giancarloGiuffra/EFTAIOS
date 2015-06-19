package it.polimi.server;

import it.polimi.client.SocketInterface;
import it.polimi.common.observer.BaseObservable;
import it.polimi.common.observer.Event;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerSocketForTwoCLients extends BaseObservable implements Runnable {
    
    GameServer server;
    Thread thread1;
    Thread thread2;
    private static final Logger LOGGER = Logger.getLogger(ServerSocketForTwoCLients.class.getName());

    
    /**
     * Costruttore
     * @param client2Thread 
     * @param client1Thread 
     */
    public ServerSocketForTwoCLients(Thread client1Thread, Thread client2Thread){
         server = new GameServer(65535,65510,1); //così currentGameRoom non può che essere l'unica sala aperta
         thread1 = client1Thread;
         thread2 = client2Thread;
    }
    
    /**
     * chiude il server
     */
    public void close(){
        server.close();
    }
    
    /**
     * per mantenere il notify di BaseObservable protected final
     * @param event
     */
    public void notifyEvent(Event event){
    	this.notify(event);
    }
    
    /**
     * fa partire il server, gestisce le due connessioni, inizializza la sal e aspetta i thread dei client
     */
    @Override
    public void run(){
        try {
            server.startServerSocketOnlyForTest(2, this.thread1, this.thread2, this);
        } catch (InterruptedException | IOException e) {
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
