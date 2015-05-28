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
			System.out.println("Scegli che interfaccia di rete usare:");
			System.out.println("1 - Socket");
			System.out.println("2 - RMI");
			scelta = stdIn.nextLine();
			if(!scelta.equals("1")  && !scelta.equals("2"))
				System.out.println("Comando non riconosciuto!");
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
    	else System.out.println("Non è stato possibile connettersi al server");
    }

}
