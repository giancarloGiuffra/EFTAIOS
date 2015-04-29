package it.polimi.model.player;

public enum Personaggio {
    CAPITANO(Razza.HUMAN,"Ennio Maria Dominoni"),
    PILOTA(Razza.HUMAN,"Julia Niguloti"),
    PSICOLOGO(Razza.HUMAN,"Silvano Porpora"),
    SOLDATO(Razza.HUMAN,"Tuccio Brendon"),
    ALIENO1(Razza.ALIEN,"Piero Ceccarella"),
    ALIENO2(Razza.ALIEN,"Vittorio Martana"),
    ALIENO3(Razza.ALIEN,"Maria Galbani"),
    ALIENO4(Razza.ALIEN,"Paolo Landon");
    
    private final Razza razza;
    private final String nome;
    
    private Personaggio(Razza razza, String nome){
        this.razza = razza; this.nome = nome;
    }
    public Razza razza(){return this.razza;}
    public String nome(){return this.nome;}
    public boolean isHuman(){return this.razza==Razza.HUMAN;}
    public boolean isAlien(){return this.razza==Razza.ALIEN;}
}
