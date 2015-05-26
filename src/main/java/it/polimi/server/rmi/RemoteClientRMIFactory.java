package it.polimi.server.rmi;

import it.polimi.common.observer.BaseObservable;
import it.polimi.common.observer.ServerNewClientRMIEvent;
import it.polimi.view.View;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RemoteClientRMIFactory extends BaseObservable implements ClientRMIFactory{
    
	private static final Logger LOGGER = Logger.getLogger(RemoteClientRMIFactory.class.getName());
	
	/**
     * Costruttore
     */
    public RemoteClientRMIFactory(){
        
    }

    @Override
    public void createNewClientRMI(String notifierName, String clientAddress, Integer port) throws RemoteException {
    	Registry registry = LocateRegistry.getRegistry(clientAddress, port);
    	try {
			RemoteNotifier notifier = (RemoteNotifier) registry.lookup(notifierName);
			ClientRMI clientRMI = new ClientRMI(notifier);
	        this.notify(new ServerNewClientRMIEvent(clientRMI));
		} catch (NotBoundException e) {
			LOGGER.log(Level.SEVERE, String.format("remote object %s not bound in client %s at port %d", notifierName, clientAddress, port ), e);
		}
    }
    
}
