package it.polimi.server;

import it.polimi.common.observer.BaseObservable;
import it.polimi.common.observer.BaseObserver;
import it.polimi.common.observer.Event;
import it.polimi.common.observer.ServerNewClientRMIEvent;
import it.polimi.server.exceptions.IllegalObservableForGameServer;
import it.polimi.server.rmi.ClientRMIFactory;
import it.polimi.server.rmi.RemoteClientRMIFactory;
import it.polimi.server.socket.ClientSocket;
import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameServer implements BaseObserver{
    
    private static final Logger LOGGER = Logger.getLogger(GameServer.class.getName());
    private static final Integer MAX_GAMEROOMS = 2;
    private final Integer portSocket;
    private final Integer portRMI;
    private ServerSocket serverSocket;
    private GameRoom currentGameRoom; 
    
    /**
     * Costruttore
     */
    public GameServer(int portSocket, int portRMI){
        this.portSocket = portSocket;
        this.portRMI = portRMI;
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
    	GameServer server = new GameServer(1337, 4040);
    	try{                
            server.startServer();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }
    
    /**
     * lancia il server
     * @throws IOException
     */
    public void startServer() throws IOException {
    	
    	//ciclo infinito
        while(true){
        	//synchronized consigliato da sonar 
        	synchronized(Thread.currentThread()){
        		//while+wait per far dormire il thread se non ci sono gameroom disponibili
                while(!(GameRoom.numberOfRooms() < MAX_GAMEROOMS)){
            		try {
    					wait();
    				} catch (InterruptedException e) {
    					LOGGER.log(Level.WARNING, "Exception in blocco wait che aspetta finche si liberi una GAMEROOM");;
    				}
            	}
                //creazione gameroom
                GameRoom gameRoom = new GameRoom(new ClientManager());
                this.currentGameRoom = gameRoom;
                //server rmi
                String clientRMIFactoryName = "ClientRMIFactory";
                try{
                	ClientRMIFactory clientRMIFactory = new RemoteClientRMIFactory();
                	( (RemoteClientRMIFactory) clientRMIFactory).addObserver(this); //aggiunge il server come observer
                	ClientRMIFactory clientRMIFactoryStub = (ClientRMIFactory) UnicastRemoteObject.exportObject(clientRMIFactory,this.portRMI);
                	LocateRegistry.createRegistry(this.portRMI);
                	Registry registry = LocateRegistry.getRegistry(this.portRMI);
                	registry.rebind(clientRMIFactoryName, clientRMIFactoryStub);
                	LOGGER.log(Level.INFO, String.format("GameServer RMI pronto in porta: %d", this.portRMI));
                } catch (Exception e) {
                	LOGGER.log(Level.SEVERE, "RMI Exception: ".concat(e.getMessage()),e);
                }
                //server socket
            	this.serverSocket = new ServerSocket(this.portSocket);
            	LOGGER.log(Level.INFO, String.format("GameServer Socket pronto in porta: %d", this.portSocket));
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

	@Override
	public void notifyRicevuto(BaseObservable source, Event event) {
		if(!(source instanceof RemoteClientRMIFactory)) throw new IllegalObservableForGameServer(String.format("%s non è un observable ammissibile per questa classe %s", source.toString(), this.toString()));
		if("ServerNewClientRMIEvent".equals(event.name())){
			if(!this.currentGameRoom.isFull()){
				this.currentGameRoom.addClient(((ServerNewClientRMIEvent)event).clientRMI());
				if(this.currentGameRoom.isFull() && !this.currentGameRoom.hasStarted()) this.currentGameRoom.start();
			} else {
				((ServerNewClientRMIEvent)event).clientRMI().write("Ci dispiace la sala è piena. Per favore prova a connetterti di nuovo. Questa connessione sarà chiusa");
			}
			
		}
	}
}
