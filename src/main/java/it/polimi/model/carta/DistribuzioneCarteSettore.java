package it.polimi.model.carta;

import it.polimi.model.exceptions.IllegalDistribuzioneCartaSettore;

/**
 * Enumeration type per la distribuzione delle carte settore
 *
 */
public enum DistribuzioneCarteSettore {
    EQUIDISTRIBUITA(8,8,9),
    BASTARDA(0,0,25);
    
    private static final int TOTALE = 25;
    private final int rumoreMio;
    private final int rumoreQualunque;
    private final int silenzio;
    
    /**
     * Costruttore
     * @param rumoreMio numero di carte RUMORE_MIO
     * @param rumoreQualunque numero di carte RUMORE_QUALUNQUE
     * @param silenzio numero di carte SILENZIO
     */
    private DistribuzioneCarteSettore(int rumoreMio, int rumoreQualunque, int silenzio){
        if(rumoreMio+rumoreQualunque+silenzio != TOTALE) throw new IllegalDistribuzioneCartaSettore(String.format("%d,%d,%d non è una distribuzione di carte settore valida", rumoreMio, rumoreQualunque, silenzio));
        this.rumoreMio = rumoreMio;
        this.rumoreQualunque = rumoreQualunque;
        this.silenzio = silenzio;
    }

    /**
     * @return numero di carte RUMORE_MIO della distribuzione
     */
    public int rumoreMio() {
        return this.rumoreMio;
    }

    /**
     * @return numero di carte RUMORE_QUALUNQUE della distribuzione
     */
    public int rumoreQualunque() {
        return this.rumoreQualunque;
    }

    /**
     * @return numero di carte SILENZIO della distribuzione
     */
    public int silenzio() {
        return this.silenzio;
    }
}
