package it.polimi.server;

import java.io.BufferedReader;
import java.io.PrintWriter;

public interface Client {
	
	void write(String message);
	
	public BufferedReader in();
	
	public PrintWriter out();
	
	public void close();

}
