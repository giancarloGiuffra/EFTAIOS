package it.polimi.server.rmi;

import it.polimi.common.observer.BaseObservable;
import it.polimi.common.observer.Event;
import it.polimi.common.observer.ServerNewClientRMIEvent;

import java.rmi.RemoteException;

public class RemoteClientRMIFactory extends BaseObservable implements ClientRMIFactory{
    
    /**
     * Costruttore
     */
    public RemoteClientRMIFactory(){
        
    }

    @Override
    public void createNewClientRMI(String RMIInterfaceName) throws RemoteException {
        // TODO Auto-generated method stub
    	ClientRMI clientRMI = new ClientRMI();
        this.notify(new ServerNewClientRMIEvent(clientRMI));
    }
    
}
