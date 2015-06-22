package it.polimi.client;

import java.io.BufferedReader;

public interface NetworkInterfaceForClient extends Runnable{
	
	/**
	 * Gestisce l'apertura della connessione
	 * @return
	 */
	Boolean connectToServer();
	
	/**
	 * Chiude la connessione
	 * @return
	 */
	Boolean close();
	
	/**
	 * stampa all'utente
	 */
	void print(String string);
	
	/**
	 * setter del buffered reader per l'input
	 * @param stdIn
	 */
	void setStdIn(BufferedReader stdIn);
	
	/**
	 * time limit per input
	 * @return
	 */
	public Integer timeLimit();

}
