package it.polimi.client;

import it.polimi.gui.*;

public class ClientGUI{
    
    private NetworkInterfaceForClient networkInterface;
    private static GUI gui;
    
    /**
     * Costruttore
     */
    private ClientGUI(){
    	gui = new GUI();
    	TipoInterface tipoInterfaccia = gui.sceltaTecnologiaDiComunicazione();
    	// networkInterface sarà null fino a quando l'utente non effettua la sua scelta
    	/*do{		// provvisorio: modificare
    		
    	}
    	while (this.networkInterface == null);*/
    	this.networkInterface = NetworkInterfaceFactory.getInterface(tipoInterfaccia); 
    	if(this.networkInterface.connectToServer()) (new Thread(this.networkInterface)).start();
        else this.comunicaConnessioneFallita();
    }
    
    /**
     * comunica all'utente della GUI che la connessione non è stata possibile
     */
    private void comunicaConnessioneFallita() {
       gui.comunicaMessaggio("Connessione fallita");    
    }
    
    public static GUI returnGUI() {
    	return gui;
    }
    
    /**
     * MAIN
     * @param args
     */
    public static void main(String[] args) {
        ClientGUI client = new ClientGUI(); 
        /*if(client.networkInterface.connectToServer()) (new Thread(client.networkInterface)).start();
        else client.comunicaConnessioneFallita();*/
    }

}
