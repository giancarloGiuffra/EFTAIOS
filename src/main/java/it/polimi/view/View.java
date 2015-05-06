package it.polimi.view;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.polimi.common.observer.BaseObservable;
import it.polimi.common.observer.BaseObserver;
import it.polimi.common.observer.Event;
import it.polimi.common.observer.UserMoveEvent;
import it.polimi.model.exceptions.GameException;
import it.polimi.model.player.AzioneGiocatore;
import it.polimi.model.player.Player;

public class View extends BaseObservable implements BaseObserver {

	private static final Pattern PATTERN_MOSSA = Pattern.compile("move to: (?<nomeSettore>.)");
	private Scanner scanner;
	private PrintStream output;
	private static final Logger LOGGER = Logger.getLogger(Player.class.getName());
	
	/**
	 * Costruttore
	 * @param inputStream
	 * @param output
	 */
	public View(InputStream inputStream, OutputStream output) {
		this.scanner = new Scanner(inputStream);
		this.output = new PrintStream(output);
	}	
	
	/**
	 * Stampa messaggio nello stream di output
	 * @param message
	 */
	private void print(String message) {
		output.println(message);		
	}
	
	/**
	 * Stampa nello stream di output un messaggio per chiedere all'utente di inserire
	 * una mossa, ricordando il format usato finche l'utente non inserisce una mossa valida
	 * dopodicchè comunica la mossa agli observers
	 */
	public void chiediMossa(){
		print("Tocca a te. Indica la tua mossa:");
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
		    return;
		} else if (azioni.size() == 1 && azioni.get(0) == AzioneGiocatore.PESCA_CARTA){
		    this.chiediDiPescareCarta();
		} else {
		    this.chiediDiScegliereAzione(azioni);
		}
	}
	
	/**
	 * Chiede all'utente di scegliere tra le azioni nella lista
	 */
	private void chiediDiScegliereAzione(List<AzioneGiocatore> azioni) {
        // TODO Auto-generated method stub
        
    }

    /**
	 * Chiede all'utente di procedere per pescare una carta
	 */
	private void chiediDiPescareCarta() {
        // TODO Auto-generated method stub
        
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
		Event event = new UserMoveEvent(matcher.group("nomeSettore"));
		try {
			this.notify(event);
		} catch (GameException ex){
			LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
		}
	}
	
	@Override
	public void notify(BaseObservable source, Event event) {
		// TODO Auto-generated method stub

	}

}
