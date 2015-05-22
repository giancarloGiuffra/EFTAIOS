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
	private PrintWriter stdOut = new PrintWriter(System.out); //NOSONAR si vuole usare System.out 
	private static final Integer PORT = 1337;
    private static final Logger LOGGER = Logger.getLogger(ClientCLI.class.getName());
    private static final String close = "CHIUSURA";
    private Boolean closed = false;

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
		print("Inserisci l'Indirizzo IP del Server: ");
		try {
			this.socket = new Socket(stdIn.nextLine(), PORT);
			this.in = new Scanner(socket.getInputStream());
			this.out = new PrintWriter(socket.getOutputStream());
		} catch (UnknownHostException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
			print("Errore nel cercare di connettersi");
		}
	}
	
	/**
	 * controlla se il server ha chiuso la connessione
	 * @return
	 */
	private Boolean isClosed(){
	    return this.closed;
	}
	
	/**
	 * stampa in std Out
	 * @param string
	 */
	private void print(String string){
	    stdOut.println(string);
	    stdOut.flush();
	}
	
	/**
	 * stampa nel server
	 * @param string
	 */
	private void printToServer(String string){
	    out.println(string);
	    out.flush();
	}

	@Override
	public void run() {
	    while(!isClosed()){
    	    String fromServer = in.nextLine();
    	    print(fromServer);
    	    while(in.hasNextLine()){
    	        fromServer = in.nextLine();
    	        print(fromServer);
    	        if(fromServer.equals(ClientCLI.close)){
    	            this.closed = true;
    	        }
    	    }
    	    printToServer(stdIn.nextLine());
    	    //TODO trovare un modo di impedire aspettare input dall'utente 
    	    //quando ad esempio il turno è finito oppure il server invia un messaggio
    	    //senza aspettare input dal client
	    }
	    this.close();
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
		(new Thread(client)).start();;
    }
	
}
