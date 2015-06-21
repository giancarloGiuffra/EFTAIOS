package it.polimi.server;

import it.polimi.client.SocketInterface;
import it.polimi.model.Model;

public class RispostaPerServer {
	
	private SocketInterface socketInterface;
	private Model model;
	
	/**
	 * Costruttore
	 * @param view
	 */
	RispostaPerServer(SocketInterface socketInterface){
		this.socketInterface = socketInterface;
	}
	
	/**
	 * setter per il model
	 * @param model
	 */
	public void setModel(Model model){
		this.model = model;
	}
		
	/**
	 * restituisce risposte per la view
	 * @param string
	 * @return
	 */
	public Risposta risposta(String string){
		if("mossa_aleatoria".equals(string))
			return new RispostaMossa(socketInterface, string,model);
		return new Risposta(socketInterface, string);
	}
		
}
