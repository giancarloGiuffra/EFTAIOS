package it.polimi.client;

public class ClientGUI{
    
    private NetworkInterfaceForClient networkInterface;
    
    /**
     * Costruttore
     */
    private ClientGUI(){
        //TODO deve inizializzare il field networkInterface tra SOCKET_GUI e RMI_GUI
    }
    
    /**
     * comunica all'utente della GUI che la connessione non è stata possibile
     */
    private void comunicaConnessioneFallita() {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * MAIN
     * @param args
     */
    public static void main(String[] args) {
        ClientGUI client = new ClientGUI();
        if(client.networkInterface.connectToServer()) (new Thread(client.networkInterface)).start();
        else client.comunicaConnessioneFallita();
    }

}
