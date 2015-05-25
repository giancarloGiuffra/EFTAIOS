package it.polimi.server.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteClientRMIFactory extends Remote {
    
    void createNewClientRMI(String RMIInterfaceName) throws RemoteException;

}
