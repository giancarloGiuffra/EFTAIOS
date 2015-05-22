package it.polimi.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameServer {
    
    private static final Logger LOGGER = Logger.getLogger(GameServer.class.getName());
    private static final Integer MAX_GAMEROOMS = 2;
    private final Integer port;
    private ServerSocket serverSocket;
    
    /**
     * Costruttore
     */
    public GameServer(int port){
        this.port = port; 
    }
    
    /**
     * MAIN
     * @param args
     */
    public static void main(String[] args) {
    	GameServer server = new GameServer(1337);
    	try{                
            server.startServer();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }
    
    public void startServer() throws IOException {
    	// apre connessione
    	this.serverSocket = new ServerSocket(this.port);
    	LOGGER.log(Level.INFO, String.format("GameServer pronto in porta: ", this.port));
    	//si mette ad ascoltare finchè ci sono GameRooms disponibili
        while(true){
        	while(!(GameRoom.numberOfRooms() < MAX_GAMEROOMS)){
        		try {
					wait();
				} catch (InterruptedException e) {
					LOGGER.log(Level.WARNING, "Exception in blocco wait che aspetta finche si liberi una GAMEROOM");;
				}
        	}
            GameRoom gameRoom = new GameRoom(new ClientManager(new Client(serverSocket.accept())));
            while(!gameRoom.isFull()){
                gameRoom.addClient(new Client(serverSocket.accept()));
            }
            gameRoom.start();
        }
    }
}
