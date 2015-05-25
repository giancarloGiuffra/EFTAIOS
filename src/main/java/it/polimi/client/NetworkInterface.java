package it.polimi.client;

public interface NetworkInterface extends Runnable{
	
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

}
