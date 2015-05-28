package it.polimi.client;

public class NetworkInterfaceFactory {
	
	/**
	 * Costruttore
	 */
	private NetworkInterfaceFactory(){
		
	}
	
	public static NetworkInterfaceForClient getInterface(TipoInterface tipo){
		switch(tipo){
    		case SOCKET:
    		    return new SocketInterface();
    		case RMI:
    		    return new RMIInterface();
    		case SOCKET_GUI:
    		    return new SOCKETGUIInterface();
    		case RMI_GUI:
    		    return new RMIGUIInterface();
		    default:
		        throw new IllegalInterfaceException();
		}
	}

}
