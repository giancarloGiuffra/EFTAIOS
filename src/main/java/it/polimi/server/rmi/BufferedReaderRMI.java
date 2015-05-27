package it.polimi.server.rmi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import it.polimi.view.BufferedReaderPlus;

public class BufferedReaderRMI extends BufferedReaderPlus {

	private ClientRMI clientRMI;

	public BufferedReaderRMI(ClientRMI client) throws FileNotFoundException {
		super(new FileReader(new File(BufferedReaderRMI.class.getName())));
		this.clientRMI = client;
		//TODO eliminare in altro modo la exception
	}

	@Override
	public String readLine() throws IOException {
		return  this.clientRMI.read();
	}

}
