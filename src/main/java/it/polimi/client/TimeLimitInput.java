package it.polimi.client;

import java.util.TimerTask;

public class TimeLimitInput extends TimerTask {

    private NetworkInterfaceForClient networkInterface;
    
    TimeLimitInput(NetworkInterfaceForClient networkInterface){
        this.networkInterface = networkInterface;
    }
    
    @Override
    public void run() {
        this.networkInterface.print("Time Limit per l'input superato");
        this.networkInterface.print("Il programma si chiuderà");
        this.networkInterface.close();
    }

}
