package it.polimi.client;

import java.util.Timer;
import java.util.TimerTask;

public class TimeLimitInput extends TimerTask {

    private NetworkInterfaceForClient networkInterface;
    private Timer timer;
    
    TimeLimitInput(NetworkInterfaceForClient networkInterface, Timer timer){
        this.networkInterface = networkInterface;
        this.timer = timer;
    }
    
    @Override
    public void run() {
        this.networkInterface.print("Time Limit per l'input superato");
        this.networkInterface.print("Il programma si chiuderà");
        this.networkInterface.close();
        this.timer.cancel();
        synchronized(this.networkInterface){
        	this.networkInterface.notifyAll();
        }
    }

}
