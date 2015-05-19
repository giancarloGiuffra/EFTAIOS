package it.polimi.view;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.polimi.common.observer.BaseObservable;
import it.polimi.common.observer.Event;
import it.polimi.common.observer.ModelAttaccoEvent;
import it.polimi.common.observer.UserAnnounceSectorEvent;
import it.polimi.common.observer.UserAttackEvent;
import it.polimi.common.observer.UserMoveEvent;
import it.polimi.common.observer.UserPicksCardEvent;
import it.polimi.common.observer.UserStartEvent;
import it.polimi.common.observer.UserTurnoFinitoEvent;
import it.polimi.model.exceptions.AzioneSceltaInaspettataException;
import it.polimi.model.exceptions.IterazioneNonPrevistaException;
import it.polimi.model.player.AzioneGiocatore;
import it.polimi.socket.Client;

public class View extends BaseObservable implements Runnable {

	private static final Pattern PATTERN_MOSSA = Pattern.compile("move to: (?<nomeSettore>.{3})");
	private static final Pattern PATTERN_ANNOUNCE = Pattern.compile("announce: (?<nomeSettore>.{3})");
	private Scanner scanner;
	private PrintWriter output;
	
	/**
	 * Costruttore
	 * @param inputStream
	 * @param output
	 */
	public View(InputStream inputStream, OutputStream output) {
		this.scanner = new Scanner(inputStream);
		this.output = new PrintWriter(output, true);
	}
	
	/**
	 * Costruttore per Client
	 * @param client
	 * @throws IOException 
	 */
	public View(Client client) throws IOException{
	    this.setScannerAndOutput(client);
	}
	
	/**
	 * Associa alla view gli stream del Client
	 * @param client
	 * @throws IOException
	 */
	public void setScannerAndOutput(Client client) throws IOException {
        this.setScanner(client.inputstream());
        this.setOutput(client.outputstream());
    }

    /**
	 * Stampa messaggio nello stream di output
	 * @param message
	 */
	public void print(String message) {
		output.println(message);		
	}
	
	/**
	 * Stampa nello stream di output un messaggio per chiedere all'utente di inserire
	 * una mossa, ricordando il format usato finche l'utente non inserisce una mossa valida
	 * dopodicchè comunica la mossa agli observers
	 */
	public void chiediMossa(){
		print("Indica la tua mossa:");
		print("Ricorda che il formato da utlizzare è:");
		print(PATTERN_MOSSA.pattern());
		String mossa = this.scanner.nextLine();
		while(!isValidMossa(mossa)){
			print("La mossa inserita non è valida. Inserirne un'altra.");
			mossa = this.scanner.nextLine();
		}
		this.sendMossa(mossa);
	}
	
	/**
	 * Chiede azione da eseguire a seconda di quelle elencate nella lista
	 * @param azioni ista di azioni
	 */
	public void chiediAzione(List<AzioneGiocatore> azioni){
		if (azioni.isEmpty()){
		    this.comunicaTurnoFinito();
		} else if (onlyActionIsPescaCarta(azioni)){
		    this.chiediDiPescareCarta();
		} else if (moreThanOneAction(azioni)){
		    this.chiediDiScegliereAzione(azioni);
		} else
		{
			throw new IterazioneNonPrevistaException("Qualcosa ci è sfuggito nell'implementare il gioco");
		}
	}
	
	/**
	 * Controlla se ci sono più di un'azione
	 * @param azioni
	 * @return
	 */
	private boolean moreThanOneAction(List<AzioneGiocatore> azioni) {
        return azioni.size() >= 2;
    }

    /**
	 * Controlla se l'unica azione presente nella lista è Pescare una Carta
	 * @param azioni
	 * @return true se la condizione è vera
	 */
	private boolean onlyActionIsPescaCarta(List<AzioneGiocatore> azioni) {
        return azioni.size() == 1 && azioni.get(0) == AzioneGiocatore.PESCA_CARTA;
    }

    /**
	 * Comunica all'utente che il turno è finito
	 */
	public void comunicaTurnoFinito() {
		print("Il tuo turno è finito.\n\n");
		this.notify(new UserTurnoFinitoEvent());
	}

	/**
	 * Chiede all'utente di scegliere tra le azioni nella lista
	 */
	private void chiediDiScegliereAzione(List<AzioneGiocatore> azioni) {
        AzioneGiocatore scelta = this.printListaAzioniERestituisceScelta(azioni);
        switch(scelta){
        case PESCA_CARTA:
        	this.chiediDiPescareCarta();
        	break;
        case ATTACCA:
        	this.notify(new UserAttackEvent());
        	break;
        case NON_ATTACCA:
            this.comunicaTurnoFinito();
            break;
        default:
        	throw new AzioneSceltaInaspettataException("Azione Scelta Non Prevista");
        }
    }

    /**
     * Fa il print delle azioni possibili e restituisce la scelta fatta dall'utente
     * @param azioni
     * @return azione scelta
     */
	private AzioneGiocatore printListaAzioniERestituisceScelta(
			List<AzioneGiocatore> azioni) {
		Map<Integer,AzioneGiocatore> mappa = printListaAzioni(azioni);
		return restituisceScelta(mappa);
	}
	
	/**
	 * Restituisce scelta fatta dall'utente
	 * @param mappa che indicizza le azioni
	 * @return azione scelta
	 */
	private AzioneGiocatore restituisceScelta(
			Map<Integer, AzioneGiocatore> mappa) {
		Pattern indexPattern = Pattern.compile("\\d+");
		String scelta = this.scanner.nextLine();
		Matcher matcher = indexPattern.matcher(scelta);
		while(!matcher.matches() ||
		        !mappa.containsKey(Integer.parseInt(scelta)) ){
		    print("Scelta non valida");
			scelta = this.scanner.nextLine();
			matcher.reset(scelta);
		}
		return mappa.get(Integer.parseInt(scelta));
	}
	
	/**
	 * Fa il print della lista di azioni e restituisce una mappa dove a ciascuna azione
	 * corrisponde un indice intero
	 * @param azioni
	 * @return mappa delle azioni
	 */
	private Map<Integer,AzioneGiocatore> printListaAzioni(List<AzioneGiocatore> azioni){
		print("Le azioni possibili sono le seguenti:"); 
		print("(inserisce il numero corrispondente per indicare la tua scelta)");
		StringBuilder opzioniBuilder = new StringBuilder();
		Map<Integer,AzioneGiocatore> mappaAzioni = new HashMap<Integer,AzioneGiocatore>();
		Integer index = new Integer(1);
		for(AzioneGiocatore azione : azioni){
			mappaAzioni.put(index, azione);
			opzioniBuilder.append(index.toString()).append(" ").append(azione.nome()).append("\n");
			index++;
		}
		print(opzioniBuilder.toString());
		return mappaAzioni;
	}

	/**
	 * Chiede all'utente di procedere per pescare una carta
	 */
	private void chiediDiPescareCarta() {
        print("Devi pescare una Carta Settore. Premi invio per procedere.");
        this.scanner.nextLine(); //Verifica se utente ha premuto invio
        Event event = new UserPicksCardEvent();
        this.notify(event);
    }

    /**
	 * Verifica che la mossa sia nel formato valido
	 * @param mossa
	 * @return true se la mossa è nel formato valido
	 */
	private static boolean isValidMossa(String mossa){
		Matcher matcher = PATTERN_MOSSA.matcher(mossa);
		return matcher.matches();
	}
	 
	/**
	 * Lancia il notify della mossa ricevuta dall'utente
	 * @param mossa
	 */
	private void sendMossa(String mossa){
		Matcher matcher = PATTERN_MOSSA.matcher(mossa);
		matcher.matches();
		Event event = new UserMoveEvent(matcher.group("nomeSettore").toUpperCase());
		this.notify(event);
	}
	
	/**
	 * Stampa il messaggio iniziale del gioco
	 */
	private void printWelcomeMessage(){
		print("Benvenuto nel gioco Fuga dagli Alieni nello Spazio Profondo.");
		print("(premi invio per iniziare)");
	}

	/**
	 * Stampa il messaggio di benvenuto
	 * Quando il giocatore preme invio lancia la notifica di inizio gioco
	 */
	@Override
	public void run() {
		this.printWelcomeMessage();
		this.scanner.nextLine(); // verifica se utente ha premuto invio
		this.notify(new UserStartEvent());
	}

	/**
	 * chiede all'utente di inserire un settore da annunciare 
	 */
	public void chiediSettoreDaAnnunciare() {
		print("Devi annunciare un settore");
		print("Ricorda che il formato da utlizzare è:");
		print(PATTERN_ANNOUNCE.pattern());
		String announce = this.scanner.nextLine();
		while(!isValidAnnouncement(announce)){
			print("L'annuncio inserito non è valido. Inserirne un altro.");
			announce = this.scanner.nextLine();
		}
		this.sendAnnouncement(announce);
	}

	/**
	 * invia il notify che il giocatore è annunciato un settore
	 * @param announce
	 */
	private void sendAnnouncement(String announce) {
		Matcher matcher = PATTERN_ANNOUNCE.matcher(announce);
		matcher.matches();
		Event event = new UserAnnounceSectorEvent(matcher.group("nomeSettore").toUpperCase());
		this.notify(event);	
	}

	/**
	 * verifica se l'annuncio di settore è valido
	 * @param announce
	 * @return
	 */
	private boolean isValidAnnouncement(String announce) {
		Matcher matcher = PATTERN_ANNOUNCE.matcher(announce);
		return matcher.matches();
	}

    /**
     * comunica al giocatore che lo spostamento è stato effettuato
     * @param settore
     */
	public void comunicaSpostamento(String settore) {
	    print(String.format("Ti sei spostato nel settore %s", settore));
    }

    /**
     * Stampa il nome della carta pescata
     * @param nomeCarta
     */
	public void comunicaCartaPescata(String nomeCarta) {
        print(String.format("Hai pescato una carta %s", nomeCarta));
    }

    /**
     * Stampa il messaggio comunicando che il silenzio è stato dichiarato
     */
	public void comunicaSilenzioDichiarato() {
        print("Hai dichiarato silenzio.");
    }

    /**
     * Comunica al giocatore che ha annunciato rumore nel settore indicato
     * @param settore
     */
	public void comunicaSettoreAnnunciato(String settore) {
        print(String.format("Hai annunciato rumore nel settore %s", settore));
    }
	
	/**
	 * setter per lo scanner
	 * @param inputstream
	 */
	public void setScanner(InputStream inputstream){
	    this.scanner = new Scanner(inputstream);
	}
	
	/**
	 * setter per l'output
	 * @param outputstream
	 */
	public void setOutput(OutputStream outputstream){
	    this.output = new PrintWriter(outputstream, true);
	}

    /**
     * comunica l'attacco effettuato al giocatore
     * @param event
     */
	public void comunicaAttaccoEffettuato(ModelAttaccoEvent event) {
        this.print(event.getMsg());
        this.notify(event); //notify per ClientManager
        
    }

}