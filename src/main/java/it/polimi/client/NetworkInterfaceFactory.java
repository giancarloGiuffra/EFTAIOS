package it.polimi.client;

public class NetworkInterfaceFactory {
	
	/**
	 * Costruttore
	 */
	private NetworkInterfaceFactory(){
		
	}
	
	public static NetworkInterfaceForClient getInterface(TipoInterface tipo){
		if(TipoInterface.SOCKET.equals(tipo)) return new SocketInterface();
		else return new RMIInterface();
	}

}
