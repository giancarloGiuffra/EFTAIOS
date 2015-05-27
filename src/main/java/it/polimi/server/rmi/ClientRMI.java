package it.polimi.server.rmi;

import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.common.observer.PrintEvent;
import it.polimi.common.observer.ReadEvent;
import it.polimi.server.Client;
import it.polimi.view.BufferedReaderPlus;
import it.polimi.view.PrintWriterPlus;

public class ClientRMI implements Client {

	private RemoteNotifier notifierToClient;
	private BufferedReaderRMI in;
	private PrintWriterRMI out;
	private static final Logger LOGGER = Logger.getLogger(ClientRMI.class.getName());
	
	public ClientRMI(RemoteNotifier notifierToClient){
		this.notifierToClient = notifierToClient;
		this.in = new BufferedReaderRMI(this);
		this.out = new PrintWriterRMI(this);
	}
	
	@Override
	public void write(String message) {
		try {
			this.notifierToClient.notifyRMIEvent(new PrintEvent(message), "server");
		} catch (RemoteException e) {
			LOGGER.log(Level.SEVERE, "Errore remoto nel writer", e);
		}
	}

	@Override
	public BufferedReaderPlus in() {
		return this.in;
	}

	@Override
	public PrintWriterPlus out() {
		return this.out;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	public String read() {
		try {
			return this.notifierToClient.notifyRMIEvent(new ReadEvent(), "server");
		} catch (RemoteException e) {
			LOGGER.log(Level.SEVERE, "Errore remoto nel reader", e);
			return "ERROR";
		}
	}

}
