package it.polimi.client;

import it.polimi.gui.GUI;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class SocketGUIInterface extends SocketInterface {
	
    private GUI gui;
	
    /**
     * Costruttore
     */
    public SocketGUIInterface(){
        gui = new GUI(this);
    }
    
    @Override
    public void run() {
		String fromServer;
		gui.visualizzaTabellone();
	    while(!isClosed()){
    	    fromServer = readLineFromServer();
    	    if(this.isCommand(fromServer)){
	    	    List<String> comandoRicevuto = getComando(fromServer);
    	    	gui.decoderComando(comandoRicevuto);
    	    }
    	    if(fromServer.equals("RICHIEDE_INPUT")){
    	        gui.countDown();
                try {
                    //Thread.sleep(TIME_LIMIT*1000);
                    synchronized(this){
                        this.wait((long) TIME_LIMIT*1000);
                    }
                } catch (InterruptedException e) {
                    LOGGER.log(Level.WARNING, e.getMessage(), e);
                }
    	    	if (gui.isInputInserito() == true)
    	    	    printToServer(gui.annunciaInput()); 
    	    	else // se l'utente non ha inserito input si chiude la connessione
    	    	    this.close();
    	    }
    	    if(fromServer.equals("CHIUSURA")) this.close();
    	    if(fromServer.equals("ERROR FROM SERVER")){
    	    	gui.comunicaMessaggio(fromServer);
    	    	this.close();
    	    }
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




