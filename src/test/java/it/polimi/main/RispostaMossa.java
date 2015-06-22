package it.polimi.main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mockito.invocation.InvocationOnMock;

import it.polimi.model.Model;
import it.polimi.view.View;

public class RispostaMossa extends Risposta {

	private static final Pattern TURNO = Pattern.compile("Tocca a te .*\\((?<personaggio>.*)\\) - Turno numero \\d{1,} - Posizione (?<posizione>.{3})");
	private Model model;
	private MainTest mainTest;
	
	/**
	 * Costruttore
	 * @param view
	 * @param string
	 * @param model
	 */
	public RispostaMossa(View view, String string, Model model, MainTest mainTest) {
		super(view, string);
		this.model = model;
		this.mainTest = mainTest;
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
			this.mainTest.addPosizione(posizione);
			this.view.setInput(risposta(risposta));
		}
		return null;
	}

}
