package it.polimi.model.player;

public enum Personaggio {
    ALIENO1(Razza.ALIEN,"Piero Ceccarella"),
    CAPITANO(Razza.HUMAN,"Ennio Maria Dominoni"),
    ALIENO2(Razza.ALIEN,"Vittorio Martana"),
    PILOTA(Razza.HUMAN,"Julia Niguloti"),
    ALIENO3(Razza.ALIEN,"Maria Galbani"),
    PSICOLOGO(Razza.HUMAN,"Silvano Porpora"),
    ALIENO4(Razza.ALIEN,"Paolo Landon"),
    SOLDATO(Razza.HUMAN,"Tuccio Brendon");
     
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
