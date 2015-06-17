package it.polimi.main;

import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.regex.Pattern;

import it.polimi.common.observer.Event;
import it.polimi.model.player.AzioneGiocatore;
import it.polimi.view.PrintWriterPlus;
import it.polimi.view.View;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Matchers;


public class MainTest {

	private static final Pattern CHIEDI_MOSSA = Pattern.compile("Indica la tua mossa:.*", Pattern.DOTALL); //DOTALL fa che . matchi anche i line terminator
	private static final Pattern MOSSA_NON_VALIDA = Pattern.compile("La mossa inserita non è valida.*");
	private static final Pattern BENVENUTO = Pattern.compile("Benvenuto.*");
	private static final Pattern TURNO = Pattern.compile("Tocca a .* Posizione (?<posizione>.{3})");
	private static final Pattern TURNO_FINITO = Pattern.compile("Il tuo turno è finito.*", Pattern.DOTALL);
	private static final Pattern PESCA_CARTA = Pattern.compile("Devi pescare una Carta Settore.*");
	private static final Pattern SCEGLIE_AZIONE = Pattern.compile("Le azioni possibili sono.*");
	private static final Pattern ANNUNCIA_SETTORE = Pattern.compile(".*RUMORE IN QUALUNQUE SETTORE");
	private View view;
	private View viewSpy;
	private RispostaPerView rispostaPerView;
	private Deque<String> posizioni;
	
	/**
	 * aggiunge posizione
	 * @param posizione
	 */
	public void addPosizione(String posizione){
		this.posizioni.add(posizione);
	}
	
	/**
	 * stampa in sytem out
	 * @param string
	 */
	private void print(String string){
	    System.out.println(string);
	}
	
	@Before
	public void inizializza(){
		view = new View();
		viewSpy = spy(view);
		viewSpy.setOutput(new PrintWriterPlus(System.out));
		rispostaPerView = new RispostaPerView(viewSpy);
		posizioni = new ArrayDeque<String>();
	}
	
	@Test
	public void testPrimoTurnoUnGiocatore(){
		
		//main specifico
		Main main = new Main(2,viewSpy);
		rispostaPerView.setModel(main.model()); //passa una reference del model per costruire le risposte
		rispostaPerView.setMainTest(this);
		
		//given
		willAnswer(rispostaPerView.risposta(System.lineSeparator())).given(viewSpy).print(matches(BENVENUTO.pattern()));
		willAnswer(rispostaPerView.risposta("mossa_aleatoria")).given(viewSpy).print(matches(TURNO.pattern()));
	    willAnswer(rispostaPerView.risposta(System.lineSeparator())).given(viewSpy).print(matches(PESCA_CARTA.pattern()));
        willAnswer(rispostaPerView.risposta("1")).given(viewSpy).print(matches(SCEGLIE_AZIONE.pattern()));
        willAnswer(rispostaPerView.risposta("announce: M09")).given(viewSpy).print(matches(ANNUNCIA_SETTORE.pattern()));
        //willAnswer(rispostaPerView.risposta("ABORT")).given(viewSpy).print(TURNO_FINITO.pattern());

		//when
		viewSpy.run();
		
		//then
		verify(viewSpy,times(1)).run();
		verify(viewSpy,atLeast(1)).chiediMossa(any(Event.class));
		verify(viewSpy,atLeast(1)).comunicaSpostamento(anyString());
		verify(viewSpy,atLeast(1)).chiediAzione(Matchers.anyListOf(AzioneGiocatore.class));
		verify(viewSpy,atLeast(1)).comunicaTurnoFinito();
		
		//print posizioni
		for(String posizione : posizioni)
		    print(posizione);
	}

}
