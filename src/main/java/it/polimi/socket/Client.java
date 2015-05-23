package it.polimi.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
    
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private static final Logger LOGGER = Logger.getLogger(ClientManager.class.getName().concat(Client.class.getSimpleName()));
    
    /**
     * Costruttore
     * @param inputstream
     * @param outputstream
     */
    public Client(Socket socket) {
        this.socket = socket;
        try{
        	this.in = new BufferedReader( new InputStreamReader(this.socket.getInputStream()));
        	this.out = new PrintWriter(this.socket.getOutputStream(), true);
        } catch (IOException ex){
        	LOGGER.log(Level.SEVERE, String.format("Errore nell'ottenere stream dal socket %s", this.socket.toString()), ex);
        }
    }
    
    /**
     * restituisce un InputStream per il client
     * @return
     */
    public InputStream inputstream() {
    	try {
    		return this.socket.getInputStream();
    	} catch (IOException ex){
        	LOGGER.log(Level.SEVERE, String.format("Errore nell'ottenere inputstream dal socket %s", this.socket.toString()), ex);
        }
		return null; //TODO forse gestire in modo diverso
    }
    
    /**
     * restituisce un OutputStream per questo client 
     * @return
     */
    public OutputStream outputstream(){
    	try{
    		return this.socket.getOutputStream();
    	} catch (IOException ex){
        	LOGGER.log(Level.SEVERE, String.format("Errore nell'ottenere inputstream dal socket %s", this.socket.toString()), ex);
        }
		return null; //TODO forse gestire in modo diverso
    }
    
    /**
     * scrive messaggio nel outputstream del client
     * @param message
     */
    public void write(String message){
        this.out.println(message);
    }
    
    /**
     * legge dal inputstream del client, restituisce ERROR se 
     * @return
     */
    public String read(){
        try {
            return this.in.readLine();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, String.format("Errore nel leggere da Client nel socket %s", this.socket.toString()), e);
            return "ERROR";
        }
    }
    
    /**
     * getter per lo scanner
     * @return
     */
    public BufferedReader in(){
        return this.in;
    }
    
    /**
     * getter per l'out
     * @return
     */
    public PrintWriter out(){
        return this.out;
    }

	/**
	 * chiude la connessione di questo client
	 */
    public void close() {
        this.write("CHIUSURA");
		this.out.close();
		try {
		    this.in.close();
			this.socket.close();
		} catch (IOException e) {
			LOGGER.log(Level.INFO, "Errore nel chiudere il scoket", e);
		}
	}
}
