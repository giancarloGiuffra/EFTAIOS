package it.polimi.client;

import it.polimi.gui.GUI;

import java.util.ArrayList;

public class SocketGUIInterface extends SocketInterface {
	
	//private GUI gui = new GUI();  // variabile provvisoria: ClientGUI restituirà quella desiderata
    private GUI gui = new GUI();
	
    @Override
    public void run() {
		String fromServer;
		gui.visualizzaTabellone();
	    while(!isClosed()){
    	    fromServer = readLineFromServer();
    	    if(this.isCommand(fromServer)){
	    	    ArrayList<String> comandoRicevuto = getComando(fromServer);
    	    	decoderComando(comandoRicevuto);
    	    }
    	    if(fromServer.equals("RICHIEDE_INPUT")){
    	    	//printToServer(this.read());
    	    }
    	    if(fromServer.equals("CHIUSURA")) this.close();
    	    if(fromServer.equals("ERROR FROM SERVER")){
    	    	gui.comunicaMessaggio(fromServer);
    	    	this.close();
    	    }
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
    
    private void decoderComando(ArrayList<String> comando) {
    	String nomeComando = comando.get(0);
    	switch(nomeComando) {
    		case "INIZIO":
    			ArrayList<String> informazioniIniziali = new ArrayList<String>();
    	    	comando.remove(0);
    	    	informazioniIniziali = comando;	// tolto il nome del comando nella prima posizione, restano solo i parametri
    			gui.ricavaInformazioniIniziali(informazioniIniziali);
    			break;
    		case "ABILITA_SETTORI":
    			break;
    		case "CONNESSIONE_PERSA":
    			gui.comunicaMessaggio(nomeComando);
    			break;
    		case "SCEGLIE_AZIONE":
    			gui.abilitaAltriPulsanti();
    			break;
    		case "RISULTATO_ATTACCO":
    			break;
    		case "CARTA_PESCATA":
    			String cartaPescata = comando.get(1);
    			gui.comunicaMessaggio("Carta pescata: " + cartaPescata);
    			break;
    		case "TURNO_FINITO":
    			gui.comunicaMessaggio("Non ci sono altre mosse possibili per il turno corrente");
    			break;
    		case "MORTO":
    			gui.comunicaMessaggio("Il tuo personaggio è morto in seguito ad un attacco");
    			break;
    		case "GIOCO_FINITO":
    			gui.comunicaMessaggio(nomeComando); // + \nvincitore?
    	}
    }
    
    
}




