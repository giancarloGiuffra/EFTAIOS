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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RMIInterface implements NetworkInterfaceForClient {

	private Scanner stdIn = new Scanner(System.in);
	private PrintWriter stdOut = new PrintWriter(System.out); //NOSONAR si vuole usare System.out 
	private static final Integer PORT = 4040; //porta di ascolto del server
    private static final Logger LOGGER = Logger.getLogger(RMIInterface.class.getName());
    private ClientRMIFactory clientRMIFactory;
	
	/**
	 * Costruttore
	 */
	public RMIInterface(){
		
	}
	
	@Override
	public Boolean connectToServer() {
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
		return true;
	}

	@Override
	public void run() {
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
		String ipAddress = getMyIPAddress();
		if(!"ERROR".equals(ipAddress)){
			try {
				RemoteNotifier notifier = new NotifierClient(this);
				RemoteNotifier notifierStub = (RemoteNotifier) UnicastRemoteObject.exportObject(notifier, port);
				Registry registry = LocateRegistry.createRegistry(port);
				String notifierName = "Notifier".concat(port.toString());
				registry.bind(notifierName, notifierStub);
				this.clientRMIFactory.createNewClientRMI(notifierName, ipAddress, port);
				print(String.format("Si è messa a disposizione la porta %s per comunicare con il server",port));
			} catch (RemoteException | AlreadyBoundException e) {
				LOGGER.log(Level.SEVERE, "RMI Exception", e);
			}
		}else{
			print("Errore nell'ottenere un indirizzo IP valido da inviare al server");
		}
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
	public void print(String string){
	    stdOut.println(string);
	    stdOut.flush();
	}
	
	/**
	 * legge da stdIn
	 * @return
	 */
	public String read(){
		return stdIn.nextLine();
	}

}
