package it.polimi.socket;

import it.polimi.common.observer.BaseObservable;
import it.polimi.common.observer.BaseObserver;
import it.polimi.common.observer.Event;
import it.polimi.model.exceptions.IllegalObservableForClientManager;
import it.polimi.view.View;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class ClientManager implements BaseObserver{
    
    private static final Integer MAX_CLIENTS = 8;
    private Queue<Client> clients;
    
    /**
     * Costruttore
     */
    public ClientManager(Client client){
        this.clients = new LinkedList<Client>();
        this.addClient(client);
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
        if(event.name().equals("UserTurnoFinitoEvent")){
            this.finishClientTurn();
            try {
                ( (View) source).setScannerAndOutput(this.currentClient());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }    
    }
    
    /**
     * Restituisce true se è pieno (massimo 8 clienti)
     * @return
     */
    public Boolean isFull(){
        return this.clients.size() >= MAX_CLIENTS;
    }
}
