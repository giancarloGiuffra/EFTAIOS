package it.polimi.model.carta;

import it.polimi.model.exceptions.IllegalDistribuzioneCartaSettore;

public enum DistribuzioneCarteSettore {
    EQUIDISTRIBUITA(8,8,9),
    BASTARDA(0,0,25);
    
    private static final int totaleCarte = 25;
    private final int rumoreMio;
    private final int rumoreQualunque;
    private final int silenzio;
    
    private DistribuzioneCarteSettore(int rumoreMio, int rumoreQualunque, int silenzio){
        if(rumoreMio+rumoreQualunque+silenzio != totaleCarte) throw new IllegalDistribuzioneCartaSettore(String.format("%d,%d,%d non è una distribuzione di carte settore valida", rumoreMio, rumoreQualunque, silenzio));
        this.rumoreMio = rumoreMio;
        this.rumoreQualunque = rumoreQualunque;
        this.silenzio = silenzio;
    }

    public int rumoreMio() {
        return this.rumoreMio;
    }

    public int rumoreQualunque() {
        return this.rumoreQualunque;
    }

    public int silenzio() {
        return this.silenzio;
    }
}
