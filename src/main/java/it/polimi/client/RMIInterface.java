package it.polimi.client;

import it.polimi.server.rmi.ClientRMIFactory;
import it.polimi.server.rmi.RemoteNotifier;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RMIInterface implements NetworkInterfaceForClient {

	private Scanner stdIn = new Scanner(System.in);
	private PrintWriter stdOut = new PrintWriter(System.out); //NOSONAR si vuole usare System.out 
	private static final Integer PORT = 65534; //porta di ascolto del server
    private static final Logger LOGGER = Logger.getLogger(RMIInterface.class.getName());
    private ClientRMIFactory clientRMIFactory;
    private Boolean closed = false;
    private static final Integer TIME_BETWEEN_CONNECTION_CHECKS = 10000; //in miliseconds
    private static final Pattern PATTERN_COMANDO = Pattern.compile("COMANDO(.+%){1,}COMANDO");
    private static final Integer TIME_LIMIT = 60; //in secondi
	
	/**
	 * Costruttore
	 */
	public RMIInterface(){
		
	}
	
	@Override
	public Boolean connectToServer() {
		return registerServerInClient() && registerClientInServer();
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
	private void checkConnectionToServer() {
		try {
			this.clientRMIFactory.checkConnection();
		} catch (RemoteException e) {
			LOGGER.log(Level.SEVERE, "Connessione con il server persa", e);
			print("Il server non risponde. Si chiuderà il programma.");
			this.close();
		}
	}

	private boolean isClosed() {
		return this.closed;
	}

	/**
	 * metodo per ottenere un indirizzo IP da inviare al server
	 * @return
	 */
	private String getMyIPAddress() {
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
	private boolean isUsable(InetAddress address) {
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
	
	private boolean mustPrint(String string) {
		return !string.equals("FINE_MESSAGGIO") && 
	           !string.equals("RICHIEDE_INPUT") &&
	           !string.equals("CHIUSURA") &&
	           !isCommand(string);
	}
	
	private boolean isCommand(String string){
	    Matcher matcher = PATTERN_COMANDO.matcher(string);
	    return matcher.matches();
	}

	/**
	 * legge da stdIn
	 * @return
	 */
	public String read(){
	    Timer timer = new Timer();
	    timer.schedule( new TimeLimitInput(this), TIME_LIMIT*1000 );
	    print("(ricorda che hai 60 secondi)");
		String input = stdIn.nextLine();
		timer.cancel();
		return input;
	}
	
	/**
	 * inizializza oggetto remoto per interagire con il server
	 */
	private Boolean registerServerInClient(){
		print("Inserisci l'Indirizzo IP del Server: ");
		String server = stdIn.nextLine();
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
	 */
	private Boolean registerClientInServer(){
		print("Inserisci la porta locale da utilizzare per la comunicazione: ");
		Pattern portPattern = Pattern.compile("\\d+");
		String portName = stdIn.nextLine();
		Matcher matcher = portPattern.matcher(portName);
		while(!matcher.matches() || !notValidPort(portName)){
			print("Porta non valida. Deve essere un numero nel range [49152,65535]. Inserisce ancora: ");
			portName = stdIn.nextLine();
			matcher.reset(portName);
		}
		Integer port = Integer.parseInt(portName);
		//String ipAddress = getMyIPAddress();
		String ipAddress = "127.0.0.1";
		if(!"ERROR".equals(ipAddress)){
			try {
				RemoteNotifier notifier = new NotifierClient(this);
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

}
