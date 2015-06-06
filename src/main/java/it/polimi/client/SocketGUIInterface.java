package it.polimi.client;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SocketGUIInterface extends SocketInterface {
    
    @Override
    public void run() {
		String fromServer;
	    while(!isClosed()){
    	    while( mustPrint(fromServer = readLineFromServer()) ){
    	    	leggiComando(fromServer);
    	    }
    	    if(fromServer.equals("RICHIEDE_INPUT")){
    	    	//printToServer(this.read());
    	    }
    	    if(fromServer.equals("CHIUSURA")) this.close();
    	    if(fromServer.equals("ERROR FROM SERVER")){
    	    	print("Il server non risponde. Si chiuderà il programma");
    	    	this.close();
    	    }
	    }
	}
    
    private void leggiComando(String fromServer){
    	 if (this.isCommand(fromServer) == true) {
    		 Pattern estrazioneDati = Pattern.compile("%[A-Z_]+");
    		 Matcher matcher = estrazioneDati.matcher(fromServer);
    		 ArrayList<String> dati = new ArrayList<String>();
    		 while(matcher.find()) {
    			 dati.add(matcher.group());
    		 }
    	 }
    	
    }
    
}




