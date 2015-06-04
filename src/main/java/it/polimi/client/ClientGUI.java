package it.polimi.client;

import it.polimi.gui.*;
import it.polimi.view.View;

public class ClientGUI{
    
    private NetworkInterfaceForClient networkInterface;
    
    /**
     * Costruttore
     */
    private ClientGUI(View view){
    	//GUI nuovaGUI = new GUI();
    	GUI nuovaGUI = view.returnGUI();
    	TipoInterface tipoInterfaccia = nuovaGUI.sceltaTecnologiaDiComunicazione();
    	this.networkInterface = NetworkInterfaceFactory.getInterface(tipoInterfaccia);
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
        /*ClientGUI client = new ClientGUI();
        if(client.networkInterface.connectToServer()) (new Thread(client.networkInterface)).start();
        else client.comunicaConnessioneFallita();*/
    }

}
