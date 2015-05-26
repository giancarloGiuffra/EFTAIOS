package it.polimi.server.rmi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;

import it.polimi.view.PrintWriterPlus;

public class PrintWriterRMI extends PrintWriterPlus {

	private ClientRMI clientRMI;
	
	public PrintWriterRMI(ClientRMI clientRMI) throws FileNotFoundException {
		super(new File(PrintWriterRMI.class.getName()));
		this.clientRMI = clientRMI;
		//TODO eliminare in altro modo exception
	}

	@Override
	public void println(String x) {
		this.clientRMI.write(x);
	}

}
