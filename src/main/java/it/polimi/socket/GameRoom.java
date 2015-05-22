package it.polimi.socket;

import java.io.IOException;

import it.polimi.controller.Controller;
import it.polimi.model.Model;
import it.polimi.view.View;

public class GameRoom {
    
    private Model model;
    private Controller controller;
    private View view;
    private ClientManager manager;
    
    private static Integer NUMBER_OF_GAMEROOMS = 0;
    
    /**
     * Costruttore
     * @param manager
     */
    public GameRoom(ClientManager manager){
        this.manager = manager;
    }
    
    /**
     * inizializza il gioco e lo lancia
     * @throws IOException
     */
    public void start() {
        this.model = new Model(this.manager.numeroGiocatori());
        this.manager.inizializza(this.model.players());
        this.view = new View(this.manager.fileIn(), this.manager.fileOut());
        this.controller = new Controller(model,view);
        this.model.addObserver(controller);
        this.view.addObserver(this.manager); //importante che manager sia il primo observer
        this.view.addObserver(controller);
        NUMBER_OF_GAMEROOMS++;
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
        return GameRoom.NUMBER_OF_GAMEROOMS;
    }
}
