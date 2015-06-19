package it.polimi.client;

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
	 * time limit per input
	 * @return
	 */
	public Integer timeLimit();

}
