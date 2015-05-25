package it.polimi.client;

public class NetworkInterfaceFactory {
	
	/**
	 * Costruttore
	 */
	private NetworkInterfaceFactory(){
		
	}
	
	public static NetworkInterface getInterface(TipoInterface tipo){
		if(TipoInterface.SOCKET.equals(tipo)) return new SocketInterface();
		else return new RMIInterface();
	}

}
