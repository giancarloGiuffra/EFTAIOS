package it.polimi.server;

import it.polimi.view.BufferedReaderPlus;
import it.polimi.view.PrintWriterPlus;

import java.io.BufferedReader;
import java.io.PrintWriter;

public interface Client {
	
	void write(String message);
	
	public BufferedReaderPlus in();
	
	public PrintWriterPlus out();
	
	public void close();

}
