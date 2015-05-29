package it.polimi.client;

import java.util.Scanner;

public class ClientCLI{
	
	private NetworkInterfaceForClient networkInterface;
	private Scanner stdIn = new Scanner(System.in);
	
	/**
	 * Costruttore
	 * @return 
	 */
	private ClientCLI(){
		String scelta = "";
		while(!scelta.equals("1") && !scelta.equals("2")){
			System.out.println("Scegli che interfaccia di rete usare:"); //NOSONAR si vuole usare system.out
			System.out.println("1 - Socket"); //NOSONAR
			System.out.println("2 - RMI"); //NOSONAR
			scelta = stdIn.nextLine();
			if(!scelta.equals("1")  && !scelta.equals("2"))
				System.out.println("Comando non riconosciuto!"); //NOSONAR
		}
		TipoInterface tipo;
		if(scelta.equals("1"))  tipo = TipoInterface.SOCKET;
		else tipo = TipoInterface.RMI;
		this.networkInterface = NetworkInterfaceFactory.getInterface(tipo);
	}
	
	/**
     * MAIN
     * @param args
     */
    public static void main(String[] args) {
    	ClientCLI client = new ClientCLI();
    	if(client.networkInterface.connectToServer()) (new Thread(client.networkInterface)).start();
    	else System.out.println("Non è stato possibile connettersi al server"); //NOSONAR
    }

}
