package it.polimi.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SocketInterface implements NetworkInterfaceForClient {

	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	private Scanner stdIn = new Scanner(System.in);
	private PrintWriter stdOut = new PrintWriter(System.out); //NOSONAR si vuole usare System.out 
	private static final Integer PORT = 65535; //porta di ascolto del server
    private static final Logger LOGGER = Logger.getLogger(SocketInterface.class.getName());
    private Boolean closed = false;
    private static final Pattern PATTERN_COMANDO = Pattern.compile("COMANDO(.+%){1,}COMANDO");
	
	/**
	 * Costruttore
	 */
	public SocketInterface(){
		
	}
	
	@Override
	public Boolean connectToServer() {
		print("Inserisci l'Indirizzo IP del Server: ");
		try {
			//this.socket = new Socket(stdIn.nextLine(), PORT);
			this.socket = new Socket("127.0.0.1", PORT);
			this.in = new BufferedReader( new InputStreamReader(socket.getInputStream()));
			this.out = new PrintWriter(socket.getOutputStream());
			return true;
		} catch (UnknownHostException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
			return false;
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
			print("Errore nel cercare di connettersi");
			return false;
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
	public Boolean close() {
		this.out.close();
		try {
		    this.in.close();
			this.socket.close();
			return true;
		} catch (IOException e) {
			LOGGER.log(Level.INFO, "Errore nel chiudere il socket", e);
			return false;
		}
	}

	@Override
	public void run() {
		String fromServer;
	    while(!isClosed()){
    	    while( mustPrint(fromServer = readLineFromServer()) ){
    	        print(fromServer);
    	    }
    	    if(fromServer.equals("RICHIEDE_INPUT")){
    	    	printToServer(stdIn.nextLine());
    	    }
    	    if(fromServer.equals("CHIUSURA")) this.closed = true;
    	    if(fromServer.equals("ERROR FROM SERVER")){
    	    	print("Il server non risponde. Si chiuderà il programma");
    	    	this.closed = true;
    	    }
	    }
	    this.close();
	}
	
	/**
	 * controlla se deve stampare per il client o meno
	 * @param string
	 * @return
	 */
	private Boolean mustPrint(String string){
	    return !string.equals("FINE_MESSAGGIO") && 
	           !string.equals("RICHIEDE_INPUT") &&
	           !string.equals("CHIUSURA") &&
	           !string.equals("ERROR FROM SERVER") &&
	           !isCommand(string);
	}
	
	private boolean isCommand(String string){
        Matcher matcher = PATTERN_COMANDO.matcher(string);
        return matcher.matches();
    }
	
	/**
	 * legge dal server
	 * @return
	 */
	private String readLineFromServer(){
	    try {
            return this.in.readLine();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Errore nel leggere dal Server", e);
            return "ERROR FROM SERVER";
        }
	}

}
