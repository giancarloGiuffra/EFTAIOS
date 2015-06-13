package it.polimi.client;

public enum Comando {
    
    INIZIO, //info inziali INIZIO%nome%settore%razza
    MUOVE, //conferma lo spostaento fatto MUOVE%settore
    ABILITA_SETTORI, //settori su cui ti puoi muovere ABILITA_SETTORI%settore1%settore2%...
    SCEGLIE_AZIONE, //azioni possibili SCEGLIE_AZIONE%azione1%azione2 , azioni sono: pesca carta, attacca, non attacca
    SILENZIO_DICHIARATO, //2 casi: senza parametro per il giocatore che lo ha dichiarato, 1 parametro per gli altri con il nome del giocatore
    SETTORE_ANNUNCIATO, //2 casi: 1 parametro per il giocatore che annuncia, 2 parametri per gli altri ovvero nome e settore
    RISULTATO_ATTACCO, // RISULTATO_ATTACCO%giocatore%settore%morto1%morto2%...
    TURNO_FINITO, //nessun parametro
    PESCA_CARTA, //nessun parametro , il server si aspetta un System.lineSeparator() per pescare la carta
    SETTORE_DA_ANNUNCIARE, //nessun parametro, il server si aspetta announce: settore
    CARTA_PESCATA, //CARTA_PESCATA%nomecarta per comunicare la carta pescata
    GIOCO_FINITO, //GIOCO_FINITO%tipoGameOver%vincitore1%vincitore2%... , tipoGameOver:UMANO_IN_SCIALUPPA,UMANI_MORTI,TURNI_FINITI,
    MORTO, //nessun parametro
    CONNESSIONE_PERSA;  //CONNESSIONE_PERSA%nomeGiocatore per comunciare agli altri giocatori

}
