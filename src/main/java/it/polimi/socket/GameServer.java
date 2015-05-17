package it.polimi.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameServer {
    
    private static final Logger LOGGER = Logger.getLogger(GameServer.class.getName());
    private static final Integer MAX_GAMEROOMS = 2;
    private static final Integer PORTNUMBER = 4321;
    
    /**
     * Costruttore
     */
    private GameServer(){
        //consigliato da sonar 
    }
    
    /*public static void main(String[] args) {
        
        try{                
            ServerSocket serverSocket = new ServerSocket(PORTNUMBER);
            while(GameRoom.numberOfRooms() < MAX_GAMEROOMS){
                Socket clientSocket = serverSocket.accept();
                GameRoom gameRoom = new GameRoom(new ClientManager(new Client(clientSocket)));
                while(!gameRoom.isFull()){
                    gameRoom.addClient(new Client(serverSocket.accept()));
                }
                gameRoom.start();
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        } 
    }*/
}
