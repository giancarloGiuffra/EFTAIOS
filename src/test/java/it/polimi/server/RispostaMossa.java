package it.polimi.server;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mockito.invocation.InvocationOnMock;

import it.polimi.client.SocketInterface;
import it.polimi.model.Model;
import it.polimi.view.View;

public class RispostaMossa extends Risposta {

	private static final Pattern TURNO = Pattern.compile("Tocca a .* Posizione (?<posizione>.{3})");
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
	 * restituisce alla view una mossa ad un settore adiacente aleatorio
	 */
	@Override
	public Void answer(InvocationOnMock invocation) throws Throwable {
		String arg = (String) invocation.getArguments()[0]; //pensato per intercettare argomento String di View.print
		Matcher matcher = TURNO.matcher(arg);
		if(matcher.matches()){
			String posizione = matcher.group("posizione");
			String risposta = String.format("move to: %s", model.tabellone().getRandomAdjacentSector(posizione).getNome());
			this.socketInterface.setStdIn(risposta(risposta));
		}
		return null;
	}

}
