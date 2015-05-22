package it.polimi.socket;

import it.polimi.common.observer.BaseObservable;
import it.polimi.common.observer.BaseObserver;
import it.polimi.common.observer.Event;
import it.polimi.common.observer.ModelAttaccoEvent;
import it.polimi.controller.Controller;
import it.polimi.model.exceptions.IllegalEventForClientManager;
import it.polimi.model.exceptions.IllegalObservableForClientManager;
import it.polimi.model.player.Player;
import it.polimi.view.View;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientManager implements BaseObserver{
    
    private static final Logger LOGGER = Logger.getLogger(GameServer.class.getName().concat(ClientManager.class.getSimpleName()));
    private static final Integer MAX_CLIENTS = 8;
    private Queue<Client> clients;
    private Map<Player,Client> players;
    private List<Client> clientsMorti;
    private File fileIn;
    private File fileOut;
    
    /**
     * Costruttore
     */
    public ClientManager(Client client){
        this.clients = new LinkedList<Client>();
        this.clientsMorti = new ArrayList<Client>();
        this.fileIn = new File(this.toString().concat("IN"));
        this.fileOut = new File(this.toString().concat("OUT"));
        this.addClient(client);
        this.welcome(client);
    }
    
    /**
     * getter di fileIn
     * @return
     */
    public File fileIn(){
        return this.fileIn;
    }
    
    /**
     * getter di fileOut
     * @return
     */
    public File fileOut(){
        return this.fileOut;
    }
    
    /**
     * aggiunge il client
     * @param client
     */
    public void addClient(Client client){
        this.clients.add(client);
    }
    
    /**
     * elimina il client
     * @param client
     */
    public void removeClient(Client client){
        this.clients.remove(client);
    }
    
    /**
     * restituisce il numero di giocatori
     * @return
     */
    public Integer numeroGiocatori(){
        return this.clients.size();
    }
    
    /**
     * restituisce il cliente che deve essere gestito
     * @return
     */
    public Client currentClient(){
        return this.clients.peek();
    }
    
    /**
     * passa al seguente cliente
     */
    public void finishClientTurn(){
        this.clients.add(this.clients.remove());
    }

    @Override
    public void notifyRicevuto(BaseObservable source, Event event) {
        if(!(source instanceof View)) throw new IllegalObservableForClientManager(String.format("%s non è un observable ammissibile per questa classe %s", source.toString(), this.toString()));
        switch(event.name()){
            case "UserTurnoFinitoEvent":
                this.gestisceUserTurnoFinitoEvent(source);
                break;
            case "ModelAttaccoEvent":
                this.gestisceModelAttaccoEvent(event);
                break;
            default:
                throw new IllegalEventForClientManager(String.format("%s non è un evento riconosciuto dalla classe ClientManager", event.name()));
        }
    }
    
    /**
     * Gestisce il risultato di un attacco
     * @param event
     */
    private void gestisceModelAttaccoEvent(Event event) {
        if( !((ModelAttaccoEvent) event).morti().isEmpty() ){
            for(Player player : ((ModelAttaccoEvent) event).morti()){
                this.removePlayer(player);
                this.clientsMorti.add(this.players.get(player));
            }
        }
        this.broadcastAllButCurrentClient(event.getMsg());
    }

    /**
     * elimina giocatore e il corrispondente client
     * @param player
     */
    private void removePlayer(Player player) {
        this.clients.remove(this.players.get(player));
        this.players.remove(player);
    }

    /**
     * Gestisce la fine del turno del client e crea la connessione con il client 
     * successivo
     * @param source
     */
    private void gestisceUserTurnoFinitoEvent(BaseObservable source) {
        this.finishClientTurn();
        ( (View) source).setScannerAndOutput(this.currentClient());
    }

    /**
     * Restituisce true se è pieno (massimo 8 clienti)
     * @return
     */
    public Boolean isFull(){
        return this.clients.size() >= MAX_CLIENTS;
    }

    /**
     * assoccia i giocatori ai client
     * @param playersList
     */
    public void createMap(List<Player> playersList) {
        this.players = new HashMap<Player,Client>();
        List<Client> clientsList = new ArrayList<Client>(this.clients);
        for(int i=0; i<playersList.size(); i++){
            players.put(playersList.get(i), clientsList.get(i));
        }
    }
    
    /**
     * inizializza il client manager
     * @param playersList
     */
    public void inizializza(List<Player> playersList){
        this.createMap(playersList);
        this.broadcast("Siamo al completo, il gioco inizia!");
    }
    
    /**
     * scrive nel outputsream dei client in gioco
     * @param message
     */
    public void broadcastVivi(String message){
        for(Client client : this.clients){
            client.write(message);
        }
    }
    
    /**
     * scrive nel ouputstream dei client morti (nel senso del gioco)
     * @param message
     */
    public void broadcastMorti(String message){
        for(Client client: this.clientsMorti){
            client.write(message);
        }
    }
    
    /**
     * scrive nel outputstream di tutti i client siano in gioco o siano morti
     * @param message
     */
    public void broadcast(String message){
        this.broadcastVivi(message);
        this.broadcastMorti(message);
    }
    
    /**
     * scrive nel outputstream di tutti i client ad eccezione di quello indicato
     * @param player
     * @param message
     */
    public void broadcastAllButClient(Client ignore, String message){
        for(Client client: this.clients){
            if(!client.equals(ignore))
                client.write(message);
        }
        this.broadcastMorti(message);
    }
    
    /**
     * scrive nel outputstream di tutti i client ad eccezione di quello corrente
     * @param message
     */
    public void broadcastAllButCurrentClient(String message){
        this.broadcastAllButClient(this.currentClient(), message);
    }
    
    /**
     * Da il benvenuto al gioco al client
     * @param client
     */
    public void welcome(Client client){
        client.write("Benvenuto nel gioco Fuga dagli Alieni nello Spazio Profondo.");
        client.write(String.format("(Al momento ci sono %d giocatori incluso te)", this.numeroGiocatori()));
    }
}