package it.polimi.server;

import it.polimi.server.socket.ClientSocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameServer {
    
    private static final Logger LOGGER = Logger.getLogger(GameServer.class.getName());
    private static final Integer MAX_GAMEROOMS = 2;
    private final Integer port;
    private ServerSocket serverSocket;
    private GameRoom currentGameRoom; 
    
    /**
     * Costruttore
     */
    public GameServer(int port){
        this.port = port; 
    }
    
    /**
     * retituisce la GameRoom attiva attualmente , cioè quella che sta ricevendo
     * giocatori
     * @return
     */
    public GameRoom currentGameRoom(){
        return this.currentGameRoom;
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
    	LOGGER.log(Level.INFO, String.format("GameServer pronto in porta: %d", this.port));
    	//si mette ad ascoltare finchè ci sono GameRooms disponibili
        while(true){
        	synchronized(Thread.currentThread()){
                while(!(GameRoom.numberOfRooms() < MAX_GAMEROOMS)){
            		try {
    					wait();
    				} catch (InterruptedException e) {
    					LOGGER.log(Level.WARNING, "Exception in blocco wait che aspetta finche si liberi una GAMEROOM");;
    				}
            	}
                GameRoom gameRoom = new GameRoom(new ClientManager());
                this.currentGameRoom = gameRoom;
                while(!gameRoom.isFull()){
                    ClientSocket clientSocket = new ClientSocket(serverSocket.accept());
                    if(!gameRoom.isFull()){
                        gameRoom.addClient(clientSocket);
                    }else {
                        clientSocket.write("Ci dispiace la sala si è riempita. Prova per favore a connetterti di nuovo. Questa connessione verrà chiusa");
                        clientSocket.close();
                        break;
                    }
                }
                if(!gameRoom.hasStarted()) gameRoom.start();
        	}
        }
    }
}
