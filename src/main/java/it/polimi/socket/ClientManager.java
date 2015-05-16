package it.polimi.socket;

import java.util.LinkedList;
import java.util.Queue;

public class ClientManager {
    
    private Queue<Client> clients;
    
    /**
     * Costruttore
     */
    public ClientManager(){
        this.clients = new LinkedList<Client>();
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
    
}
