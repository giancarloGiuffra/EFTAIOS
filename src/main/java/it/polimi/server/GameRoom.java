package it.polimi.server;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import it.polimi.common.observer.BaseObservable;
import it.polimi.common.observer.BaseObserver;
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
    
    private Model model;
    private ModelView modelView;
    private Controller controller;
    private View view;
    private ClientManager manager;
    private Boolean hasStarted = false;
    
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
        this.manager.inizializza(this.model.players());
        this.view = new View(this.manager.currentClient().in(), this.manager.currentClient().out());
        this.controller = new Controller(modelView,view);
        this.controller.addObserver(this);
        this.model.addObserver(controller);
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
     * aggiunge il giocatore alla sala
     * @param client
     */
    public void addClient(Client client){
        this.manager.addClient(client);
    }
    
    /**
     * lancia il gioco
     */
    private void run() {        
        (new Thread(view)).start();     
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

	@Override
	public void notifyRicevuto(BaseObservable source, Event event) {
		if(!(source instanceof ClientManager) && !(source instanceof Controller)) throw new IllegalObservableForGameRoom(String.format("%s non è un observable ammissibile per questa classe %s", source.toString(), this.toString()));
		switch(event.name()){
			case "ServerCloseGameRoom":
				GameRoom.NUMBER_OF_GAMEROOMS.decrementAndGet();
				this.notify(new ServerGameRoomTurnedAvailable());
				break;
			case "ControllerUpdateModel":
				this.model = new Model(((ControllerUpdateModel)event).model());
				break;
			default:
				break;
		}
	}
}
