package it.polimi.server.rmi;

import it.polimi.common.observer.RMIEvent;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteNotifier extends Remote {
	
	String notifyRMIEvent(RMIEvent event, String source) throws RemoteException;

}
