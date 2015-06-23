package it.polimi.client;

import it.polimi.gui.GUI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RMIGUIInterface extends RMIInterface {
	
	private transient GUI gui;
	private Boolean hasPrintBeenCalled = false;
    
    /**
     * Costruttore
     */
    public RMIGUIInterface(){
        gui = new GUI(this);
    }
	
	@Override
    public void run() {
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
        if(isFirstCallToPrint())
            gui.visualizzaTabellone();
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
     * true se è la prima volta che si chiama print
     * @return
     */
    private boolean isFirstCallToPrint() {
        if(!hasPrintBeenCalled){
            this.hasPrintBeenCalled = true;
            return true;
        }
        return false;
    }

    /**
     * @return
     * @throws IOException 
     */
    @Override
    public String read() throws IOException{
        gui.countDown();
        while(!gui.isInputInserito() && !this.isClosed()){
	        try {
	            synchronized(this){
	                this.wait((long) TIME_LIMIT*1000);
	            }
	        } catch (InterruptedException e) {
	            LOGGER.log(Level.WARNING, e.getMessage(), e);
	        }
        }
        if(gui.isInputInserito() == true)
            return gui.annunciaInput(); //generalizzare
        else{ //se l'utente non ha inserito input si chiude la connessione
            this.close();
            return "ABORT";
        }
    }
    
    private List<String> getComando(String fromServer){
        List<String> datiComando = new ArrayList<String>();
        String splitStringa[] = fromServer.split("%");
        for (int i = 0; i < splitStringa.length; i++) {
            if (splitStringa[i].equals("COMANDO") == false && splitStringa[i].isEmpty() == false) {
                datiComando.add(fromServer.split("%")[i]);
            }
        }
        return datiComando;
    }

}
