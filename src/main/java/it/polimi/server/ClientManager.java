package it.polimi.server;

import it.polimi.client.Comando;
import it.polimi.common.observer.BaseObservable;
import it.polimi.common.observer.BaseObserver;
import it.polimi.common.observer.Event;
import it.polimi.common.observer.ModelAnnunciatoSettoreEvent;
import it.polimi.common.observer.ModelAttaccoEvent;
import it.polimi.common.observer.ModelDichiaratoSilenzioEvent;
import it.polimi.common.observer.ServerCloseGameRoom;
import it.polimi.common.observer.ServerConnessionePersaConClient;
import it.polimi.model.player.Player;
import it.polimi.server.exceptions.IllegalObservableForClientManager;
import it.polimi.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class ClientManager extends BaseObservable implements BaseObserver{
    
    private static final Integer MAX_CLIENTS = 2;
	private static final Integer MIN_NUMBER_CLIENTS = 2;
    private Queue<Client> clients;
    private BiMap<Player,Client> players;
    private List<Client> clientsMorti;
    private File fileIn;
    private File fileOut;
	private List<Client> clientsDisconnected;
    
    /**
     * Costruttore
     */
    public ClientManager(){
        this.clients = new LinkedList<Client>();
        this.clientsMorti = new ArrayList<Client>();
        this.clientsDisconnected = new ArrayList<Client>();
        this.fileIn = new File(this.toString().concat("IN"));
        this.fileOut = new File(this.toString().concat("OUT"));
    }
    
    /**
     * Costruttore
     */
    public ClientManager(Client client){
        this();
        this.addClient(client);
    }
    
    /**
     * getter di fileIn
     * @return
     */
    public File fileIn(){
        return this.fileIn;
    }
    
    /**
     * getter di fileOut
     * @return
     */
    public File fileOut(){
        return this.fileOut;
    }
    
    /**
     * aggiunge il client
     * @param client
     */
    public void addClient(Client client){
        this.clients.add(client);
        this.welcome(client);
    }
    
    /**
     * elimina il client
     * @param client
     */
    public void removeClient(Client client){
        this.clients.remove(client);
    }
    
    /**
     * restituisce il numero di giocatori
     * @return
     */
    public Integer numeroGiocatori(){
        return this.clients.size();
    }
    
    /**
     * restituisce il cliente che deve essere gestito
     * @return
     */
    public Client currentClient(){
        return this.clients.peek();
    }
    
    /**
     * passa al seguente cliente
     */
    public void finishClientTurn(){
        this.clients.add(this.clients.remove());
    }

    @Override
    public void notifyRicevuto(BaseObservable source, Event event) {
        if(!(source instanceof View)) throw new IllegalObservableForClientManager(String.format("%s non è un observable ammissibile per questa classe %s", source.toString(), this.toString()));
        switch(event.name()){
            case "UserTurnoFinitoEvent":
                this.gestisceUserTurnoFinitoEvent(source);
                break;
            case "ModelAttaccoEvent":
                this.gestisceModelAttaccoEvent(source, event);
                break;
            case "ModelDichiaratoSilenzioEvent":
            	this.gestisceModelDichiaratoSilenzioEvent(source, event);
            	break;
            case "ModelAnnunciatoSettoreEvent":
            	this.gestisceModelAnnunciatoSettoreEvent(source, event);
            	break;
            case "ModelGameOver":
            	this.gestisceModelGameOver(source, event);
            	break;
            case "ServerConnessionePersaConClient":
            	this.gestisceServerConnessionePersaConClient(source);
            	break;
            default:
                break;
            }
    }
    
    private void gestisceServerConnessionePersaConClient( BaseObservable source) {
		if(this.clients.isEmpty()) return;
    	BiMap<Client, Player> clientsToPlayers = this.players.inverse();
		this.broadcastAllButCurrentClient(String.format("Abbiamo perso la connessione con %s", clientsToPlayers.get(this.clients.peek()).nome()));
		this.broadcastCommandConnessionePersa(source);
		this.clientsDisconnected.add(this.clients.remove());
		if(!this.clients.isEmpty())
			( (View) source).setInputAndOutput(this.currentClient());
		else
			this.notify(new ServerCloseGameRoom());
	}

	private void broadcastCommandConnessionePersa(BaseObservable source) {
		View view = (View) source;
		BiMap<Client, Player> clientsToPlayers = this.players.inverse();
		this.broadcastAllButCurrentClient(view.buildCommand(Comando.CONNESSIONE_PERSA, clientsToPlayers.get(this.clients.peek()).nome()));
	}

	/**
     * comunica ai giocatori che il gioco è finito
     * @param event
     */
    private void gestisceModelGameOver(BaseObservable source, Event event) {
    	this.broadcastAllButCurrentClient("Il gioco è finito");
		this.broadcastAllButCurrentClient(event.getMsg());
		this.broadcastCommandGiocoFinito(source, event);
		this.broadcast("La connessione si chiuderà tra breve");
		this.close();
		this.notify(new ServerCloseGameRoom());
	}

	private void broadcastCommandGiocoFinito(BaseObservable source, Event event) {
		View view = (View) source;
		this.broadcastAllButCurrentClient(view.buildCommand(Comando.GIOCO_FINITO, view.buildListaArgsGiocoFinito(event)));
	}

	/**
	 * chiude le connessioni con i client
	 */
    private void close() {
		this.closeClients();
		this.closeClientsMorti();
	}

	/**
	 * chiude le connessioni con i client morti
	 */
    private void closeClientsMorti() {
		for(Client client : this.clientsMorti){
			client.close();
		}
	}

	/**
	 * chiude le connessioni con i client vivi
	 */
    private void closeClients() {
		for(Client client : this.clients){
			client.close();
		}
	}

	/**
     * comunica ai giocatori chi ha annunciato rumore in quale settore
     * @param event
     */
    private void gestisceModelAnnunciatoSettoreEvent(BaseObservable source, Event event) {
    	ModelAnnunciatoSettoreEvent annuncioEvent = (ModelAnnunciatoSettoreEvent) event;
    	this.broadcastAllButCurrentClient(String.format("%s dichiara RUMORE IN SETTORE %s", annuncioEvent.player(), annuncioEvent.settore()));
    	this.broadcastCommandSettoreAnnunciato(source,event);
	}

	private void broadcastCommandSettoreAnnunciato(BaseObservable source,
			Event event) {
    	View view = (View) source;
    	ModelAnnunciatoSettoreEvent annuncioEvent = (ModelAnnunciatoSettoreEvent) event;
    	List<String> args = new ArrayList<String>();
    	args.add(annuncioEvent.player());
    	args.add(annuncioEvent.settore());
    	this.broadcastAllButCurrentClient(view.buildCommand(Comando.SETTORE_ANNUNCIATO, args));
	}

	/**
     * comunica ai giocatori chi ha dichiarato silenzio
     * @param event
     */
    private void gestisceModelDichiaratoSilenzioEvent(BaseObservable source, Event event) {
    	ModelDichiaratoSilenzioEvent silenzioEvent = (ModelDichiaratoSilenzioEvent) event;
    	this.broadcastAllButCurrentClient(String.format("%s dichiara SILENZIO", silenzioEvent.player()));
    	this.broadcastCommandSilenzioDichiarato(source, event);
	}

	private void broadcastCommandSilenzioDichiarato(BaseObservable source,
			Event event) {
		View view = (View) source;
		ModelDichiaratoSilenzioEvent silenzioEvent = (ModelDichiaratoSilenzioEvent) event;
		this.broadcastAllButCurrentClient(view.buildCommand(Comando.SILENZIO_DICHIARATO, silenzioEvent.player()));
	}

	/**
     * Gestisce il risultato di un attacco
     * @param event
     */
    private void gestisceModelAttaccoEvent(BaseObservable source, Event event) {
        if( !((ModelAttaccoEvent) event).morti().isEmpty() ){
            for(Player player : ((ModelAttaccoEvent) event).morti()){
                this.removePlayer(player);
                this.clientsMorti.add(this.players.get(player));
            }
        }
        this.broadcastAllButCurrentClient(event.getMsg());
        this.broadcastCommandRisultatoAttacco(source, event);
        this.broadcastMorti("Sei Morto :( la connessione rimarrà comunque aperta finchè il gioco non finisce");
        this.broadcastCommandMorto(source);
    }

    private void broadcastCommandMorto(BaseObservable source) {
    	View view = (View) source;
		this.broadcastMorti(view.buildCommand(Comando.MORTO));
	}

	private void broadcastCommandRisultatoAttacco(BaseObservable source,
			Event event) {
    	View view = (View) source;
    	this.broadcastAllButCurrentClient(view.buildCommand(Comando.RISULTATO_ATTACCO, view.buildListaArgsAttacco(event)));
	}

	/**
     * elimina giocatore e il corrispondente client
     * @param player
     */
    private void removePlayer(Player player) {
        this.clients.remove(this.players.get(player));
        this.players.remove(player);
    }

    /**
     * Gestisce la fine del turno del client e crea la connessione con il client 
     * successivo
     * @param source
     */
    private void gestisceUserTurnoFinitoEvent(BaseObservable source) {
        this.finishClientTurn();
        ( (View) source).setInputAndOutput(this.currentClient());
    }

    /**
     * Restituisce true se è pieno (massimo 8 clienti)
     * @return
     */
    public Boolean isFull(){
        return this.clients.size() >= MAX_CLIENTS;
    }

    /**
     * assoccia i giocatori ai client
     * @param playersList
     */
    public void createMap(List<Player> playersList) {
        this.players = HashBiMap.create();
        List<Client> clientsList = new ArrayList<Client>(this.clients);
        for(int i=0; i<playersList.size(); i++){
            players.put(playersList.get(i), clientsList.get(i));
        }
    }
    
    /**
     * inizializza il client manager
     * @param playersList
     */
    public void inizializza(List<Player> playersList){
        this.createMap(playersList);
        this.broadcast("Siamo al completo, il gioco inizia!");
    }
    
    /**
     * scrive nel outputsream dei client in gioco
     * @param message
     */
    public void broadcastVivi(String message){
        for(Client client : this.clients){
            client.write(message);
        }
    }
    
    /**
     * scrive nel ouputstream dei client morti (nel senso del gioco)
     * @param message
     */
    public void broadcastMorti(String message){
        for(Client client: this.clientsMorti){
            client.write(message);
        }
    }
    
    /**
     * scrive nel outputstream di tutti i client siano in gioco o siano morti
     * @param message
     */
    public void broadcast(String message){
        this.broadcastVivi(message);
        this.broadcastMorti(message);
    }
    
    /**
     * scrive nel outputstream di tutti i client ad eccezione di quello indicato
     * @param player
     * @param message
     */
    public void broadcastAllButClient(Client ignore, String message){
        for(Client client: this.clients){
            if(!client.equals(ignore))
                client.write(message);
        }
        this.broadcastMorti(message);
    }
    
    /**
     * scrive nel outputstream di tutti i client ad eccezione di quello corrente
     * @param message
     */
    public void broadcastAllButCurrentClient(String message){
        this.broadcastAllButClient(this.currentClient(), message);
    }
    
    /**
     * Da il benvenuto al gioco al client
     * @param client
     */
    public void welcome(Client client){
        client.write("Benvenuto nel gioco Fuga dagli Alieni nello Spazio Profondo.");
        client.write(String.format("(Al momento ci sono %d giocatori incluso te)", this.numeroGiocatori()));
    }

	/**
	 * true se ci sono almeno due client
	 * @return
	 */
    public Boolean hasAtLeastMinimumNumberOfClients() {
		return this.clients.size() >= MIN_NUMBER_CLIENTS;
	}

	/**
	 * getter per il minimo numero di clients (per far iniziare il gioco)
	 * @return
	 */
    public Integer minimumNumber() {
		return MIN_NUMBER_CLIENTS;
	}

	/**
	 * true se c'è un solo client
	 * @return
	 */
    public boolean hasOneClient() {
		return this.clients.size() == 1;
	}
}