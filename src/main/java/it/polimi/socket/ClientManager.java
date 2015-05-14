package it.polimi.socket;

import java.util.ArrayList;
import java.util.List;

public class ClientManager {
    
    private List<Client> clients;
    
    /**
     * Costruttore
     */
    public ClientManager(){
        this.clients = new ArrayList<Client>();
    }
}
