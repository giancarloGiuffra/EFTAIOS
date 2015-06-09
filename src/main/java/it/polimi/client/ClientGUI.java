package it.polimi.client;

import it.polimi.gui.*;

public class ClientGUI{
    
    private NetworkInterfaceForClient networkInterface;
    private GUISceltaInterfaccia interfaccia;
    
    /**
     * Costruttore
     */
    private ClientGUI(){
        interfaccia = new GUISceltaInterfaccia();
    	TipoInterface tipoInterfaccia = interfaccia.sceltaTecnologiaDiComunicazione();
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
        interfaccia.comunicaConnessioneFallita();    
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
