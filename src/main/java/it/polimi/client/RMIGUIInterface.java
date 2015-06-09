package it.polimi.client;

import java.util.logging.Level;
import java.util.logging.Logger;

public class RMIGUIInterface extends RMIInterface {
	
	private static final Integer TIME_BETWEEN_CONNECTION_CHECKS = 10000; //in miliseconds
	private static final Logger LOGGER = Logger.getLogger(RMIInterface.class.getName());
	
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

}
