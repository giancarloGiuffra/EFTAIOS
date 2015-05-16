package it.polimi.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {
    
    private static final Integer MAX_GAMEROOMS = 2;
    private static final Integer PORTNUMBER = 4321;
    
    public static void main(String[] args) {
        
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
    }
}
