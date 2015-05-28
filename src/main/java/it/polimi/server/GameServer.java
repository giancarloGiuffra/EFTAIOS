package it.polimi.server;

import it.polimi.common.observer.BaseObservable;
import it.polimi.common.observer.BaseObserver;
import it.polimi.common.observer.Event;
import it.polimi.common.observer.ServerNewClientRMIEvent;
import it.polimi.server.exceptions.IllegalObservableForGameServer;
import it.polimi.server.rmi.ClientRMI;
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
    	
    	//creazione gameroom
    	this.setCurrentGameRoom(new GameRoom(new ClientManager()));
    	
    	
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
        while(true){
                ClientSocket clientSocket = new ClientSocket(serverSocket.accept());
                synchronized(this){
	                if(!this.currentGameRoom.isFull()){
	                	this.currentGameRoom.addClient(clientSocket);
	                    if(this.currentGameRoom.isFull() && !this.currentGameRoom.hasStarted()){
	                    	GameRoom gameRoomToLaunch = makeNewGameRoomAvailable();
	                    	gameRoomToLaunch.start();
	                    }
	                }else {
	                    clientSocket.write("Ci dispiace l'ultima sala si è riempita. Prova per favore a connetterti più tardi. Questa connessione verrà chiusa");
	                    clientSocket.close();
	                }
                } //synchronized per evitare che RMI e Socket cerchino di aggiungere un client alla sala quando c'è solo l'ultimo posto disponibile
        } //while(true)
    }

	/**
	 * set del field currentGameRoom
	 * @param gameRoom
	 */
    private void setCurrentGameRoom(GameRoom gameRoom) {
		this.currentGameRoom = gameRoom;
		this.currentGameRoom.addObserver(this);
	}

	/**
	 * Crea una nuova gameroom e la rende disponibile nel field currentGameRoom
	 * se non ci sono più gameroom disponibili lascia il field nel suo stato attuale
	 * @return gameroom precedente 
	 */
    private GameRoom makeNewGameRoomAvailable() {
		if(!(GameRoom.numberOfRooms() < MAX_GAMEROOMS)) return this.currentGameRoom;
		GameRoom previousGameRoom = this.currentGameRoom;
		this.setCurrentGameRoom( new GameRoom(new ClientManager()) );
		return previousGameRoom;
	}

	@Override
	public void notifyRicevuto(BaseObservable source, Event event) {
		if(!(source instanceof RemoteClientRMIFactory) && !(source instanceof GameRoom)) throw new IllegalObservableForGameServer(String.format("%s non è un observable ammissibile per questa classe %s", source.toString(), this.toString()));
		switch(event.name()){
			case "ServerNewClientRMIEvent":
				this.gesticeServerNewClientRMIEvent(event);
				break;
			case "ServerGameRoomTurnedAvailable":
				this.gesticeServerGameRoomTurnedAvailable();
				break;
			default:
				break;
		}
	}

	/**
	 * gestisce il fatto che si sta rendendo disponibile una gameroom
	 */
	private void gesticeServerGameRoomTurnedAvailable() {
		makeNewGameRoomAvailable();
	}

	/**
	 * gestisce l'arrivo di un nuovo client RMI
	 * @param event
	 */
	private void gesticeServerNewClientRMIEvent(Event event) {
		synchronized(this){
			if(!this.currentGameRoom.isFull()){
				this.currentGameRoom.addClient(((ServerNewClientRMIEvent)event).clientRMI());
				if(this.currentGameRoom.isFull() && !this.currentGameRoom.hasStarted()){
					GameRoom gameRoomToLaunch = makeNewGameRoomAvailable();
					gameRoomToLaunch.start();
				}
			} else {
				ClientRMI clientRMI = ((ServerNewClientRMIEvent)event).clientRMI();
				clientRMI.write("Ci dispiace l'ultima sala si è riempita. Prova per favore a connetterti più tardi. Questa connessione verrà chiusa");
				clientRMI.close();
			}
		} //synchronized per evitare che RMI e Socket cerchino di aggiungere un client alla sala quando c'è solo l'ultimo posto disponibile
	}
	
}
