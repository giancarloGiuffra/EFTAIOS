package it.polimi.socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientCLI implements Runnable{
	
	private Socket socket;
	private Scanner in;
	private PrintWriter out;
	private Scanner stdIn = new Scanner(System.in);
	private PrintWriter stdOut = new PrintWriter(System.out);
	private static final Integer PORT = 1337;
    private static final Logger LOGGER = Logger.getLogger(ClientCLI.class.getName());

	
	/**
	 * Costruttore
	 */
	public ClientCLI(){
		//TODO per adesso non fa niente
	}
	
	/**
	 * Tenta di connettersi al server
	 */
	public void connectToServer(){
		stdOut.println("Inserisci l'Indirizzo IP del Server: ");
		try {
			this.socket = new Socket(stdIn.nextLine(), PORT);
			this.in = new Scanner(socket.getInputStream());
			this.out = new PrintWriter(socket.getOutputStream());
		} catch (UnknownHostException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
			stdOut.println("Errore nel cercare di connettersi");
		}
	}

	@Override
	public void run() {
		//TODO
	}
	
	/**
	 * chiude la connessione
	 */
	private void close(){
		this.in.close();
		this.out.close();
		try {
			this.socket.close();
		} catch (IOException e) {
			LOGGER.log(Level.INFO, "Errore nel chiudere il socket", e);;
		}
	}
	
	/**
     * MAIN
     * @param args
     */
    public static void main(String[] args) {
    	ClientCLI client = new ClientCLI();
    	client.connectToServer();
		client.run();
    }
	

}
