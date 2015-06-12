package it.polimi.main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class EstraePosizione implements Answer<Void> {

	private static final Pattern TURNO = Pattern.compile("Tocca a .* Posizione (?<posizione>.{3})");
	private MainTest mainTest;
	
	/**
	 * Costruttore
	 * @param mainTest
	 */
	public EstraePosizione(MainTest mainTest) {
		this.mainTest = mainTest;
	}

	/**
	 * aggiorna il field posizione di mainTest se l'argomento del metodo chiamato soddisfa il regex TURNO
	 */
	@Override
	public Void answer(InvocationOnMock invocation) throws Throwable {
		String arg = (String) invocation.getArguments()[0]; //pensato per intercettare argomento String di View.print
		Matcher matcher = TURNO.matcher(arg);
		if(matcher.matches()){
			String posizione = matcher.group("posizione");
			this.mainTest.setPosizione(posizione);
		}
		return null;
	}

}
