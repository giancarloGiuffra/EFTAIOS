package it.polimi.server;

import it.polimi.client.Comando;
import it.polimi.common.observer.BaseObservable;
import it.polimi.common.observer.BaseObserver;
import it.polimi.common.observer.Event;
import it.polimi.common.observer.ServerNewClientRMIEvent;
import it.polimi.common.observer.SetModelInGameServerTest;
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
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameServer implements BaseObserver{
    
    private static final Logger LOGGER = Logger.getLogger(GameServer.class.getName());
    private Integer MAX_GAMEROOMS;
    private Integer MAX_NUMBER_CLIENTS_PER_ROOM;
    private final Integer portSocket;
    private final Integer portRMI;
    private ServerSocket serverSocket;
    private GameRoom currentGameRoom;
    private Deque<GameRoom> gameRooms = new ArrayDeque<GameRoom>();
    
    /**
     * Costruttore
     */
    public GameServer(int portSocket, int portRMI, int maxGameRooms){
        this.portSocket = portSocket;
        this.portRMI = portRMI;
        this.MAX_GAMEROOMS = maxGameRooms;
    }
    
    /**
     * Costruttore
     */
    public GameServer(int portSocket, int portRMI){
        this(portSocket,portRMI,2); //default 2 game rooms
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
    	GameServer server = new GameServer(65535, 65533);
    	try{                
            server.startServer(2); //default 8 giocatori al massimo per game room
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }
    
    /**
     * lancia il server
     * @param maxNumberOfClientsPerRoom numero massimo di clients per room
     * @throws IOException
     */
    public void startServer(int maxNumberOfClientsPerRoom) throws IOException {
    	
    	this.MAX_NUMBER_CLIENTS_PER_ROOM = maxNumberOfClientsPerRoom;
    	
    	//creazione gameroom
    	this.setCurrentGameRoom(new GameRoom(new ClientManager(MAX_NUMBER_CLIENTS_PER_ROOM)));
    	
    	
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
	                if(!lastGameRoomAvailableHasStarted()){
	                	this.currentGameRoom.addClient(clientSocket);
	                    if(this.currentGameRoom.isFull() && !this.currentGameRoom.hasStarted()){
	                    	GameRoom gameRoomToLaunch = makeNewGameRoomAvailable();
	                    	gameRoomToLaunch.cancelTimer();
	                    	gameRoomToLaunch.start();
	                    }
	                }else {
	                    clientSocket.write("Ci dispiace l'ultima sala si è riempita. Prova per favore a connetterti più tardi. Questa connessione verrà chiusa");
	                    clientSocket.write(buildCommandNoGameRoomAvailable());
	                    clientSocket.close();
	                }
                } //synchronized per evitare che RMI e Socket cerchino di aggiungere un client alla sala quando c'è solo l'ultimo posto disponibile
        } //while
    }

	/**
	 * set del field currentGameRoom
	 * @param gameRoom
	 */
    private void setCurrentGameRoom(GameRoom gameRoom) {
		this.currentGameRoom = gameRoom;
		this.gameRooms.add(currentGameRoom);
		this.currentGameRoom.addObserver(this);
	}

	/**
	 * Crea una nuova gameroom e la rende disponibile nel field currentGameRoom
	 * se non ci sono più gameroom disponibili lascia il field nel suo stato attuale
	 * @return gameroom precedente o, nel caso nn ci siano gameroom disponibili, la current game room (cioè la game room da far partire)
	 */
    private GameRoom makeNewGameRoomAvailable() {
		if(!(GameRoom.numberOfRooms() < MAX_GAMEROOMS)) return this.currentGameRoom;
		GameRoom previousGameRoom = this.currentGameRoom;
		this.setCurrentGameRoom( new GameRoom(new ClientManager(MAX_NUMBER_CLIENTS_PER_ROOM)) );
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
				this.gesticeServerGameRoomTurnedAvailable((GameRoom) source);
				break;
			default:
				break;
		}
	}

	/**
	 * gestisce il fatto che si sta rendendo disponibile una gameroom
	 * @param gameRoom 
	 */
	private void gesticeServerGameRoomTurnedAvailable(GameRoom gameRoom) {
		synchronized(this){ //synchronized così i thread che segnalano che la corrispondente game room è chiusa si sincronizzano
			if(lastGameRoomAvailableHasStarted()){
				this.gameRooms.remove(gameRoom);
				makeNewGameRoomAvailable();
			} else {
				this.gameRooms.remove(gameRoom);
			}
		}
	}

	/**
	 * gestisce l'arrivo di un nuovo client RMI
	 * @param event
	 */
	private void gesticeServerNewClientRMIEvent(Event event) {
		synchronized(this){
			if(!lastGameRoomAvailableHasStarted()){
				this.currentGameRoom.addClient(((ServerNewClientRMIEvent)event).clientRMI());
				if(this.currentGameRoom.isFull() && !this.currentGameRoom.hasStarted()){
					GameRoom gameRoomToLaunch = makeNewGameRoomAvailable();
					gameRoomToLaunch.cancelTimer();
					gameRoomToLaunch.start();
				}
			} else {
				ClientRMI clientRMI = ((ServerNewClientRMIEvent)event).clientRMI();
				clientRMI.write("Ci dispiace l'ultima sala si è riempita. Prova per favore a connetterti più tardi. Questa connessione verrà chiusa");
				clientRMI.write(buildCommandNoGameRoomAvailable());
				clientRMI.close();
			}
		} //synchronized per evitare che RMI e Socket cerchino di aggiungere un client alla sala quando c'è solo l'ultimo posto disponibile
	}

	/**
	 * comando che segnala che non ci sono game room disponibili
	 * @return
	 */
	private String buildCommandNoGameRoomAvailable() {
		return new StringBuilder().append("COMANDO%").
				append(Comando.NESSUNA_GAMEROOM_DISPONIBILE.toString()).append("%").
                append("COMANDO").toString();
	}

	/**
	 * true se tutte l'ultima sala disponibile si è riempita e di conseguenza è già stata lanciata
	 * @return
	 */
	private boolean lastGameRoomAvailableHasStarted() {
		return (this.gameRooms.size() == MAX_GAMEROOMS) &&
				this.gameRooms.peekLast().hasStarted();
	}
	
	
    
    /**
     * lancia il server solo per RMI connection
     * @throws IOException
     */
    public void startServerRMIOnlyForTest(int maxNumberOfClientsPerRoom) throws IOException {
    	
    	this.MAX_NUMBER_CLIENTS_PER_ROOM = maxNumberOfClientsPerRoom;
    	
    	//creazione gameroom
    	this.setCurrentGameRoom(new GameRoom(new ClientManager(MAX_NUMBER_CLIENTS_PER_ROOM)));
    	
    	
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
    	
    }

    /**
     * chiude il server
     */
    public void close() {
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Si chiude il server", e);
        }
    }

    
    /*public void startServerSocketOnlyForTest(int maxNumberOfClientsPerRoom, Thread thread1, Thread thread2, ServerSocketForTwoCLients serverSocketForTwoCLients) throws IOException, InterruptedException {
        
        this.MAX_NUMBER_CLIENTS_PER_ROOM = maxNumberOfClientsPerRoom;
        
        //creazione gameroom
        this.setCurrentGameRoom(new GameRoom(new ClientManager(MAX_NUMBER_CLIENTS_PER_ROOM)));
        
        //server socket
        this.serverSocket = new ServerSocket(this.portSocket);
        LOGGER.log(Level.INFO, String.format("GameServer Socket pronto in porta: %d", this.portSocket));
        int counter = 2;
        while(counter>0){
                ClientSocket clientSocket = new ClientSocket(serverSocket.accept());
                synchronized(this){
                    if(!lastGameRoomAvailableHasStarted()){
                        this.currentGameRoom.addClient(clientSocket);
                        if(this.currentGameRoom.hasAtLeastMinimumNumberOfClients() && !this.currentGameRoom.hasStarted()){
                            this.currentGameRoom.cancelTimer();
                            this.currentGameRoom.setUp();
                        }
                    }else {
                        clientSocket.write("Ci dispiace l'ultima sala si è riempita. Prova per favore a connetterti più tardi. Questa connessione verrà chiusa");
                        clientSocket.write(buildCommandNoGameRoomAvailable());
                        clientSocket.close();
                    }
                } //synchronized per evitare che RMI e Socket cerchino di aggiungere un client alla sala quando c'è solo l'ultimo posto disponibile
                counter--;
        } //while
    }*/
	
}
