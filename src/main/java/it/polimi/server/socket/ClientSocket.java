package it.polimi.server.socket;

import it.polimi.server.Client;
import it.polimi.server.ClientManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientSocket implements Client{
    
    private Socket socket;
    private BufferedReaderSocket in;
    private PrintWriterSocket out;
    private static final Logger LOGGER = Logger.getLogger(ClientManager.class.getName().concat(ClientSocket.class.getSimpleName()));
    
    /**
     * Costruttore
     * @param inputstream
     * @param outputstream
     */
    public ClientSocket(Socket socket) {
        this.socket = socket;
        try{
        	this.in = new BufferedReaderSocket(this.socket.getInputStream());
        	this.out = new PrintWriterSocket(this.socket.getOutputStream(), true);
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
    @Override
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
    @Override
    public BufferedReaderSocket in(){
        return this.in;
    }
    
    /**
     * getter per l'out
     * @return
     */
    @Override
    public PrintWriterSocket out(){
        return this.out;
    }

	/**
	 * chiude la connessione di questo client
	 */
    @Override
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
