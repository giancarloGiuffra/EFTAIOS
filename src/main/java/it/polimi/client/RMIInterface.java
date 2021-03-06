package it.polimi.client;

import it.polimi.server.GameServer;
import it.polimi.server.rmi.ClientRMIFactory;
import it.polimi.server.rmi.RemoteNotifier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RMIInterface implements NetworkInterfaceForClient {

	private BufferedReader stdIn;
	protected PrintWriter stdOut = new PrintWriter(System.out); //NOSONAR si vuole usare System.out 
	private static final Integer PORT = 65533; //porta di ascolto del server
    protected static final Logger LOGGER = Logger.getLogger(RMIInterface.class.getName());
    private ClientRMIFactory clientRMIFactory;
    private RemoteNotifier notifier;
    private Boolean closed = false;
    protected static final Integer TIME_BETWEEN_CONNECTION_CHECKS = 10000; //in miliseconds
    private static final Pattern PATTERN_COMANDO = Pattern.compile("COMANDO%(.+%){1,}COMANDO");
    protected static final Integer TIME_LIMIT = 300; //in secondi
	protected static final long TIME_BETWEEN_INPUT_CHECKS = 1; //in secondi
	
	/**
	 * Costruttore
	 */
	public RMIInterface(){
		stdIn = new BufferedReader(new InputStreamReader(System.in));
	}
	
	/**
	 * Costruttore
	 * @param stdIn
	 */
	public RMIInterface(BufferedReader stdIn){
	    this.stdIn = stdIn;
	}
	
	/**
	 * setter per stdIn
	 * @param stdIn
	 */
	@Override
	public void setStdIn(BufferedReader stdIn){
	    this.stdIn = stdIn;
	}
	
	/**
	 * getter per TIME_LIMIT
	 * @return
	 */
	@Override
	public Integer timeLimit(){
		return TIME_LIMIT;
	}
	
	@Override
	public Boolean connectToServer() {
		try {
			return registerServerInClient() && registerClientInServer();
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		    return false;
		}
	}

	@Override
	public Boolean close() {
		try {
			UnicastRemoteObject.unexportObject(notifier, true);
		} catch (NoSuchObjectException e) {
			LOGGER.log(Level.WARNING, "Errore nel chiudere l'oggetto remoto notifierStub", e);
		}
		this.closed = true;
	    return true;
	}

	@Override
	public void run() {
	    while(!isClosed()){
	    	try {
				synchronized(this){
					this.wait(TIME_BETWEEN_CONNECTION_CHECKS);
					this.checkConnectionToServer();
				}
			} catch (InterruptedException e) {
				LOGGER.log(Level.WARNING, "Exception in blocco wait di RMIInterface");
			}
	    }
	}
	
	/**
	 * Controlla se il serve è ancora attivo, in caso negativo comunica all'utente
	 * di tale condizione e chiude il programma
	 */
	protected void checkConnectionToServer() {
		try {
			this.clientRMIFactory.checkConnection();
		} catch (RemoteException e) {
			LOGGER.log(Level.SEVERE, "Connessione con il server persa", e);
			print("Il server non risponde. Si chiuderà il programma.");
			this.close();
		}
	}

	/**
	 * true se il client è chiuso
	 * @return
	 */
	protected boolean isClosed() {
		return this.closed;
	}

	/**
	 * stampa in std Out
	 * @param string
	 */
	@Override
	public void print(String string){
	    if(mustPrint(string)){
			stdOut.println(string);
		    stdOut.flush();
	    }
	    if("CHIUSURA".equals(string)){
	        this.close();
	        synchronized(this){
	            this.notifyAll();
	        }
	    }
	}
	
	/**
	 * controlla se deve stampare a video la string
	 * @param string
	 * @return
	 */
	protected boolean mustPrint(String string) {
		return !string.equals("FINE_MESSAGGIO") && 
	           !string.equals("RICHIEDE_INPUT") &&
	           !string.equals("CHIUSURA") &&
	           !isCommand(string);
	}
	
	/**
	 * controlla se è un comando
	 * @param string
	 * @return
	 */
	protected boolean isCommand(String string){
	    Matcher matcher = PATTERN_COMANDO.matcher(string);
	    return matcher.matches();
	}

	/**
	 * legge da stdIn
	 * @return
	 * @throws IOException 
	 */
	public String read() throws IOException{
	    
	    //timer
	    Timer timer = new Timer();
	    timer.schedule( new TimeLimitInput(this, timer), TIME_LIMIT*1000 );
	    print(String.format("(hai %s secondi)",TIME_LIMIT));
	    
	    //ciclo per attendere l'input
	    while(!stdIn.ready() && !isClosed()){
			synchronized(this){
				try {
					this.wait(TIME_BETWEEN_INPUT_CHECKS*1000);
				} catch (InterruptedException e) {
					LOGGER.log(Level.WARNING, "InterruptedException in wait in read", e);
				}
			}
		}
	    
	    //controllo l'input
        if(!isClosed()){
        	String input = stdIn.readLine();
            timer.cancel();
            if(input.equals("ABORT")) this.close(); //client decide di chiudere
            return input;
        } else {
        	return "ABORT";
        }
		
	}
	
	/**
	 * inizializza oggetto remoto per interagire con il server
	 * @throws IOException 
	 */
	private Boolean registerServerInClient() throws IOException{
		
	    //chiedi IP del server
		String ipAddress = readIpAddress();;
		
		//get registry del server
		Registry registry;
		try {
			registry = LocateRegistry.getRegistry(ipAddress, PORT);
		} catch (RemoteException e){
			LOGGER.log(Level.SEVERE, "Non è stato possibile connettersi al server", e);
			return false;
		}
		
	    //oggetto remoto nel server
		String clientRMIFactoryName = "ClientRMIFactory";
		try {
			this.clientRMIFactory = (ClientRMIFactory) registry.lookup(clientRMIFactoryName);
		} catch(RemoteException e){
			LOGGER.log(Level.SEVERE, "Remote Exception", e);
			return false;
		} catch (NotBoundException e){
			LOGGER.log(Level.SEVERE, "Oggetto non bound", e);
			return false;
		}
		return true;
	}
	
	/**
     * chiede l'ip address del server finchè l'utente non inserisce un ip valido (in formato)
     * @return
     */
    private String readIpAddress() {
        try {    
            richiedeIndirizzoIpServer();
            String ipAddress = stdIn.readLine();
            while(!isValidIpAddress(ipAddress)){
                richiedeIndirizzoIpServer();
                ipAddress = stdIn.readLine();
            }
            return ipAddress;
        } catch (IOException ex){
            LOGGER.log(Level.SEVERE, "errore di lettura dal stdIn nell'inserimento dell'IP address del server", ex);
            return "Errore di Lettura";
        }
    }

    /**
     * Controlla che la string inserita sia un indirizzo ip (in formato) valido
     * @param ipAddress
     * @return
     */
    private boolean isValidIpAddress(String ipAddress) {
        final Pattern formatoIndirizzoIP = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
        Matcher matcher = formatoIndirizzoIP.matcher(ipAddress);
        return matcher.matches();
    }
	
	private void richiedeIndirizzoIpServer() {
        stdOut.println("Inserisci l'Indirizzo IP del Server: ");
        stdOut.flush();
    }

    /**
	 * crea notifier per poter ricevere richiesta dal server
	 * @return
	 * @throws IOException 
	 */
	private Boolean registerClientInServer(){
		//cerca port libero
	    Integer port;
		try{
		    port = getValidPort();
		} catch (IllegalStateException e){
		    LOGGER.log(Level.SEVERE, "Non è stato trovare una porta libera", e);
            return false;
		}
		
		//trova ip address da inviare al server
		String ipAddress;
        try {
            ipAddress = GameServer.getPrivateIpAddress();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Non è stato possibile calcolare l'ip da inviare al server", e);
            return false;
        }
        
        //register client in server
	    try {
			this.notifier = new NotifierClient(this);
			RemoteNotifier notifierStub = (RemoteNotifier) UnicastRemoteObject.exportObject(notifier, port);
			Registry registry = LocateRegistry.createRegistry(port);
			String notifierName = "Notifier".concat(port.toString());
			registry.bind(notifierName, notifierStub);
			this.clientRMIFactory.createNewClientRMI(notifierName, ipAddress, port);
			return true;
		} catch (RemoteException | AlreadyBoundException e) {
			LOGGER.log(Level.SEVERE, "RMI Exception", e);
			return false;
		}
	}

    /**
     * trova un port disponibile
     * @return
     * @throws IllegalStateException
     */
	private int getValidPort() throws IllegalStateException {
        try {
            ServerSocket temporary = new ServerSocket(0);
            temporary.setReuseAddress(true);
            int port = temporary.getLocalPort();
            temporary.close();
            return port;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Port libero non trovato", e);
        }
        throw new IllegalStateException("Non è stato possibile trovare una porta");
    }

}
