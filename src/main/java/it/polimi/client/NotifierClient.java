package it.polimi.client;

import java.io.IOException;
import java.rmi.RemoteException;

import it.polimi.common.observer.RMIEvent;
import it.polimi.server.exceptions.ServerUnknownRMIEvent;
import it.polimi.server.rmi.RemoteNotifier;

public class NotifierClient implements RemoteNotifier {

	private transient RMIInterface rmiInterface;
	
	/**
	 * Costruttore
	 * @param rmiInterface
	 */
	public NotifierClient(RMIInterface rmiInterface){
		this.rmiInterface = rmiInterface;
	}
	
	@Override
	public String notifyRMIEvent(RMIEvent event, String source)
			throws RemoteException {
		switch(event.name()){
			case "PrintEvent":
				this.rmiInterface.print(event.getMsg());
				return "OK";
			case "ReadEvent":
			try {
				return this.rmiInterface.read();
			} catch (IOException e) {
				return "ABORT";
			}
			default:
				throw new ServerUnknownRMIEvent(event.name());
		}

	}

}
