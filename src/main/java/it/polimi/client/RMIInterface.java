package it.polimi.client;

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

	private BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
	private PrintWriter stdOut = new PrintWriter(System.out); //NOSONAR si vuole usare System.out 
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

	/**
	 * controlla se un port è valido
	 * @param port
	 * @return
	 */
	private boolean notValidPort(String portName) {
		Integer port = Integer.parseInt(portName);
		return (port >= 49152) && (port <= 65535);
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

	protected boolean isClosed() {
		return this.closed;
	}

	/**
	 * metodo per ottenere un indirizzo IP da inviare al server
	 * @return
	 */
	public static String getMyIPAddress() {
		Enumeration<NetworkInterface> interfaces;
		try {
			interfaces = NetworkInterface.getNetworkInterfaces();
			while(interfaces.hasMoreElements()){
				NetworkInterface n = (NetworkInterface) interfaces.nextElement();
				Enumeration<InetAddress> addresses = n.getInetAddresses();
				while(addresses.hasMoreElements()){
					InetAddress address = (InetAddress) addresses.nextElement();
					if(isUsable(address)) return address.getHostAddress();
					else
						try {
							return InetAddress.getLocalHost().getHostAddress();
						} catch (UnknownHostException e) {
							LOGGER.log(Level.SEVERE, "Problema con InetAddress", e);
							return "ERROR";
						}
				}
			}
		} catch (SocketException e) {
			LOGGER.log(Level.SEVERE, "Problema in getMyIPAddress", e);
			return "ERROR";
		}
		return "ERROR";
	}

	/**
	 * metodo di supporto per verificare se l'indirizzo IP "può" servire
	 * @param address
	 * @return
	 */
	private static boolean isUsable(InetAddress address) {
		return !address.isLoopbackAddress() &&
				!address.isLinkLocalAddress() &&
				!address.isSiteLocalAddress() &&
				!address.isMulticastAddress();
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
	
	protected boolean mustPrint(String string) {
		return !string.equals("FINE_MESSAGGIO") && 
	           !string.equals("RICHIEDE_INPUT") &&
	           !string.equals("CHIUSURA") &&
	           !isCommand(string);
	}
	
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
	    Timer timer = new Timer();
	    timer.schedule( new TimeLimitInput(this, timer), TIME_LIMIT*1000 );
	    print(String.format("(hai %s secondi)",TIME_LIMIT));
	    while(!stdIn.ready() && !isClosed()){
			synchronized(this){
				try {
					this.wait(TIME_BETWEEN_INPUT_CHECKS*1000);
				} catch (InterruptedException e) {
					LOGGER.log(Level.WARNING, "InterruptedException in wait in read", e);
				}
			}
		}
        if(!isClosed()){
        	String input = stdIn.readLine();
            timer.cancel();
            if(input.equals("ABORT")) this.close(); //cleint decide di chiudere
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
		print("Inserisci l'Indirizzo IP del Server: ");
		//String server = stdIn.readLine();
		String server = "127.0.0.1";
		String clientRMIFactoryName = "ClientRMIFactory";
		Registry registry;
		try {
			registry = LocateRegistry.getRegistry(server, PORT);
		} catch (RemoteException e){
			LOGGER.log(Level.SEVERE, "Non è stato possibile connettersi al server", e);
			return false;
		}
		
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
	 * crea notifier per poter ricevere richiesta dal server
	 * @return
	 * @throws IOException 
	 */
	private Boolean registerClientInServer() throws IOException{
		Integer port = getValidPort();
		String ipAddress = getMyIPAddress();
		if(!"ERROR".equals(ipAddress)){
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
		}else{
			print("Errore nell'ottenere un indirizzo IP valido da inviare al server");
			return false;
		}
	}

    /**
     * trova un port disponibile
     * @return
     */
	private int getValidPort(){
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
