package it.polimi.server;

import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicInteger;

import it.polimi.common.observer.BaseObservable;
import it.polimi.common.observer.BaseObserver;
import it.polimi.common.observer.ControllerRispristinaModelView;
import it.polimi.common.observer.ControllerUpdateModel;
import it.polimi.common.observer.Event;
import it.polimi.common.observer.ServerGameRoomTurnedAvailable;
import it.polimi.controller.Controller;
import it.polimi.model.Model;
import it.polimi.model.ModelView;
import it.polimi.server.exceptions.IllegalObservableForClientManager;
import it.polimi.server.exceptions.IllegalObservableForGameRoom;
import it.polimi.view.View;

public class GameRoom extends BaseObservable implements BaseObserver{
    
    private static final Integer TIME_LIMIT_FOR_START = 5; // in minuti
	private Model model;
    private ModelView modelView;
    private Controller controller;
    private View view;
    private ClientManager manager;
    private Boolean hasStarted = false;
    private Boolean hasFinished = false;
    private Timer timer;
    
    private static AtomicInteger NUMBER_OF_GAMEROOMS = new AtomicInteger(0);
    
    /**
     * Costruttore
     * @param manager
     */
    public GameRoom(ClientManager manager){
        this.manager = manager;
        this.manager.addObserver(this);
        NUMBER_OF_GAMEROOMS.incrementAndGet();
    }
    
    /**
     * inizializza il gioco e lo lancia
     * @throws IOException
     */
    public void start() {
        this.model = new Model(this.manager.numeroGiocatori());
        this.modelView = new ModelView(this.model);
        this.manager.inizializza(this.model.players(), this.model.posizioni());
        this.view = new View(this.manager.currentClient().in(), this.manager.currentClient().out());
        this.controller = new Controller(modelView,view);
        this.controller.addObserver(this);
        this.modelView.addObserver(controller);
        this.view.addObserver(this.manager); //importante che manager sia il primo observer
        this.view.addObserver(controller);
        this.hasStarted = true;
        this.run();
    }
    
    /**
     * restituisce true se la sala è piena
     * @return
     */
    public Boolean isFull(){
        return this.manager.isFull();
    }
    
    /**
     * true se si può far partire il gioco (al meno due partecipanti)
     * @return
     */
    public Boolean canStart(){
    	return this.manager.hasAtLeastMinimumNumberOfClients();
    }
    
    /**
     * aggiunge il giocatore alla sala
     * @param client
     */
    public void addClient(Client client){
        this.manager.addClient(client);
        if(this.manager.hasOneClient()){
        	timer = new Timer();
        	TimeLimitStartGameRoom task = new TimeLimitStartGameRoom(this);
        	timer.schedule(task, (long) TIME_LIMIT_FOR_START*60*1000);
        }
    }
    
    /**
     * lancia il gioco
     */
    private void run() {        
        (new Thread(view)).start();     
    }
    
    /**
     * chiude la sala e notifica che una sala si è liberata
     */
    void close(){
    	GameRoom.NUMBER_OF_GAMEROOMS.decrementAndGet();
		this.notify(new ServerGameRoomTurnedAvailable());
    	this.hasFinished = true;
    }
    
    /**
     * indica se la sala è chiusa
     * @return
     */
    public Boolean hasFinished(){
    	return this.hasFinished;
    }
    
    /**
     * retituisce il numero di sale create
     * @return
     */
    public static Integer numberOfRooms(){
        return GameRoom.NUMBER_OF_GAMEROOMS.get();
    }
    
    /**
     * 
     * @return true se la sala ha già iniziato
     */
    public Boolean hasStarted(){
        return this.hasStarted;
    }
    
    /**
     * getter per il ClientManager
     * @return
     */
    public ClientManager clientManager(){
    	return this.manager;
    }
    
    /**
     * cancella le task associate al timer
     */
    public void cancelTimer(){
    	this.timer.cancel();
    }

	@Override
	public void notifyRicevuto(BaseObservable source, Event event) {
		if(!(source instanceof ClientManager) && !(source instanceof Controller)) throw new IllegalObservableForGameRoom(String.format("%s non è un observable ammissibile per questa classe %s", source.toString(), this.toString()));
		switch(event.name()){
			case "ServerCloseGameRoom":
				this.close();
				break;
			case "ControllerUpdateModel":
				this.model = new Model(((ControllerUpdateModel)event).model());
				break;
			case "ControllerRispristinaModelView":
				this.controller.setModelView(this.model);
				break;
			default:
				break;
		}
	}
}
