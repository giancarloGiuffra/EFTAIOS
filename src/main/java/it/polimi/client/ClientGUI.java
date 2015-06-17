package it.polimi.client;

import it.polimi.gui.*;

public class ClientGUI{
    
    private NetworkInterfaceForClient networkInterface;
    private GUISceltaInterfaccia interfaccia;
    
    /**
     * Costruttore
     */
    private ClientGUI(){
        interfaccia = new GUISceltaInterfaccia(this);
    }
    
    /**
     * comunica all'utente della GUI che la connessione non è stata possibile
     */
    private void comunicaConnessioneFallita() {
        interfaccia.comunicaConnessioneFallita();    
    }
    
    public void runNetworkInterface(TipoInterface tipo){
        this.networkInterface = NetworkInterfaceFactory.getInterface(tipo);
        if(this.networkInterface.connectToServer()) (new Thread(this.networkInterface)).start();
        else this.comunicaConnessioneFallita();
    }
    
    /**
     * MAIN
     * @param args
     */
    public static void main(String[] args) {
        new ClientGUI(); 
    }

}
