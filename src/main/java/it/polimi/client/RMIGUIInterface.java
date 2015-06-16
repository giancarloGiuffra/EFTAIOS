package it.polimi.client;

import it.polimi.gui.GUI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RMIGUIInterface extends RMIInterface {
	
	private GUI gui;
    
    /**
     * Costruttore
     */
    public RMIGUIInterface(){
        gui = new GUI(this);
    }
	
	@Override
    public void run() {
	    gui.visualizzaTabellone();
	    while(!isClosed()){
	    	try {
				synchronized(this){
					this.wait(TIME_BETWEEN_CONNECTION_CHECKS);
					this.checkConnectionToServer();
				}
			} catch (InterruptedException e) {
				LOGGER.log(Level.WARNING, "Exception in blocco wait di RMIInterface");
			}
	    }
	}
	
	/**
     * @param string
     */
    @Override
    public void print(String string){
        if(isCommand(string)){
            gui.decoderComando(getComando(string));
        }
        if("CHIUSURA".equals(string)){
            this.close();
            synchronized(this){
                this.notifyAll();
            }
        }
    }
    
    /**
     * @return
     * @throws IOException 
     */
    @Override
    public String read() throws IOException{
        gui.countDown();
        try {
            synchronized(this){
                this.wait(TIME_LIMIT*1000);
            }
        } catch (InterruptedException e) {
            LOGGER.log(Level.WARNING, e.getMessage(), e);
        }
        if(gui.isInputInserito() == true)
            return gui.annunciaInput(); //generalizzare
        else{ //se l'utente non ha inserito input si chiude la connessione
            this.close();
            return "ABORT";
        }
    }
    
    private ArrayList<String> getComando(String fromServer){
        ArrayList<String> datiComando = new ArrayList<String>();
        String splitStringa[] = fromServer.split("%");
        for (int i = 0; i < splitStringa.length; i++) {
            if (splitStringa[i].equals("COMANDO") == false && splitStringa[i].isEmpty() == false) {
                datiComando.add(fromServer.split("%")[i]);
            }
        }
        return datiComando;
    }

}
