package it.polimi.server.rmi;

import java.io.File;
import java.io.FileNotFoundException;
import it.polimi.view.PrintWriterPlus;

public class PrintWriterRMI extends PrintWriterPlus {

	private ClientRMI clientRMI;
	
	/**
	 * Costruttore
	 * @param clientRMI
	 */
	public PrintWriterRMI(ClientRMI clientRMI){
		this.clientRMI = clientRMI;
	}

	@Override
	public void println(String x) {
		this.clientRMI.write(x);
	}

}
