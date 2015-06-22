package it.polimi.server;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mockito.invocation.InvocationOnMock;

import it.polimi.client.SocketInterface;
import it.polimi.model.Model;
import it.polimi.view.View;

public class RispostaMossa extends Risposta {

	private static final Pattern TURNO = Pattern.compile("Tocca a te .*\\((?<personaggio>.*)\\) - Turno numero \\d{1,} - Posizione (?<posizione>.{3})");
	private Model model;
	
	/**
	 * Costruttore
	 * @param view
	 * @param string
	 * @param gameServerTest 
	 */
	public RispostaMossa(SocketInterface socketInterface, String string, Model model) {
		super(socketInterface, string);
		this.model = model;
	}
	
	/**
	 * true se la string contiene la substring "alien"
	 * @param personaggio
	 * @return
	 */
	private static Boolean isAlien(String personaggio){
		return personaggio.toLowerCase().contains("alien");
	}
	
	/**
	 * restituisce alla view una mossa ad un settore adiacente aleatorio
	 */
	@Override
	public Void answer(InvocationOnMock invocation) throws Throwable {
		String arg = (String) invocation.getArguments()[0]; //pensato per intercettare argomento String di View.print
		Matcher matcher = TURNO.matcher(arg);
		if(matcher.matches()){
			String posizione = matcher.group("posizione");
			String risposta;
			if (isAlien(matcher.group("personaggio")))
				risposta = String.format("move to: %s", model.tabellone().getRandomAdjacentSectorForAlien(posizione).getNome());
			else
				risposta = String.format("move to: %s", model.tabellone().getRandomAdjacentSectorForHuman(posizione).getNome());

			this.socketInterface.setStdIn(risposta(risposta));
		}
		return null;
	}

}
