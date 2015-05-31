package it.polimi.common.observer;

import java.util.List;

public class RichiediMossaEvent extends Event {

    private List<String> settori;
    
    public RichiediMossaEvent(List<String> settori) {
        super("evento richiedi mossa");
        this.settori = settori;
    }
    
    public List<String> settori(){
        return this.settori;
    }

}
