package it.polimi.common.observer;

import it.polimi.server.rmi.ClientRMI;

public class ServerNewClientRMIEvent extends Event {

	private ClientRMI clientRMI;
	
	/**
	 * Costruttore
	 * @param client
	 */
	public ServerNewClientRMIEvent(ClientRMI client){
		this("nuovo client RMI");
		this.clientRMI = client;
	}
	
	/**
	 * Costruttore
	 * @param msg
	 */
	public ServerNewClientRMIEvent(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 
	 * @return client RMI
	 */
	public ClientRMI clientRMI(){
		return this.clientRMI;
	}

}
