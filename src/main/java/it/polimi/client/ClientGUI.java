package it.polimi.client;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

import it.polimi.gui.*;

public class ClientGUI{
    
    private NetworkInterfaceForClient networkInterface;
    private GUISceltaInterfaccia interfaccia;
    
    /**
     * Costruttore
     */
    public ClientGUI(){
        interfaccia = new GUISceltaInterfaccia(this);
    }
    
    /**
     * lancia la GUI
     */
    public void start(){
    	this.interfaccia.start();
    }
    
    /**
     * comunica all'utente della GUI che la connessione non è stata possibile
     */
    private void comunicaConnessioneFallita() {
        interfaccia.comunicaConnessioneFallita();    
    }
    
    public void runNetworkInterface(TipoInterface tipo){
        this.networkInterface = NetworkInterfaceFactory.getInterface(tipo);
        this.networkInterface.setStdIn(stringToBufferedReader(interfaccia.getIP()));
        if(this.networkInterface.connectToServer()) (new Thread(this.networkInterface)).start();
        else this.comunicaConnessioneFallita();
    }
    
    /**
     * crea un buffered reader con la string
     * @param string
     * @return
     */
    private BufferedReader stringToBufferedReader(String string) {
        return  new BufferedReader(new InputStreamReader(new ByteArrayInputStream(string.getBytes())));
    }

    /**
     * MAIN
     * @param args
     */
    public static void main(String[] args) { 
        new ClientGUI().start(); 
    }
    
    public GUISceltaInterfaccia getInterfaccia() {
    	return this.interfaccia;
    }

}
