//package it.polimi.model.gioco;
//
//import it.polimi.model.player.Personaggio;
//
//public class Partita {
//    
//    private int numeroGiocatori;
//    private Personaggio listaPersonaggi[] = Personaggio.values();
//    private Personaggio listaGiocatori[];
//    private boolean terminePartita = false; // vedi classe "Turno"
//    
//    public Partita (int numeroGiocatori) {
//        this.numeroGiocatori = numeroGiocatori;
//        listaGiocatori = new Personaggio[numeroGiocatori];
//        for (int i = 0; i < numeroGiocatori; i++) {
//            listaGiocatori[i] = listaPersonaggi[i];
//        }
//        
//    }
//    
//    public Personaggio[] getListaGiocatori() {
//        return listaGiocatori; 
//    }
//    
//    public int getNumeroGiocatori() {
//        return numeroGiocatori;
//    }
//    
//}