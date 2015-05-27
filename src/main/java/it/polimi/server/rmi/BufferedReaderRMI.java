package it.polimi.server.rmi;


import java.io.IOException;
import it.polimi.view.BufferedReaderPlus;

public class BufferedReaderRMI extends BufferedReaderPlus {

	private ClientRMI clientRMI;

	public BufferedReaderRMI(ClientRMI client){
		this.clientRMI = client;
	}

	@Override
	public String readLine() throws IOException {
		return  this.clientRMI.read();
	}

}
