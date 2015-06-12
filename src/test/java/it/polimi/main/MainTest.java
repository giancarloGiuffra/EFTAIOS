package it.polimi.main;

import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;

import java.util.regex.Pattern;

import it.polimi.common.observer.Event;
import it.polimi.view.PrintWriterPlus;
import it.polimi.view.View;

import org.junit.BeforeClass;
import org.junit.Test;


public class MainTest {

	private static final Pattern CHIEDI_MOSSA = Pattern.compile("Indica la tua mossa:.*", Pattern.DOTALL); //DOTALL fa che . matchi anche i line terminator
	private static final Pattern MOSSA_NON_VALIDA = Pattern.compile("La mossa inserita non è valida.*");
	private static final Pattern BENVENUTO = Pattern.compile("Benvenuto.*");
	private static final Pattern TURNO = Pattern.compile("Tocca a .* Posizione (?<posizione>.{3})");
	private static final Pattern TURNO_FINITO = Pattern.compile("Il tuo turno è finito.*", Pattern.DOTALL);
	private static View view;
	private static View viewSpy;
	private static RispostaPerView rispostaPerView;
	private String posizione;
	
	/**
	 * setter per posizione
	 * @param posizione
	 */
	public void setPosizione(String posizione){
		this.posizione = posizione;
	}
	
	@BeforeClass
	public static void inizializza(){
		view = new View();
		viewSpy = spy(view);
		viewSpy.setOutput(new PrintWriterPlus(System.out));
		rispostaPerView = new RispostaPerView(viewSpy);
	}
	
	@Test
	public void test(){
		
		//main specifico
		Main main = new Main(1,viewSpy);
		rispostaPerView.setModel(main.model()); //passa una reference del model per costruire le risposte
		
		//given
		willAnswer(rispostaPerView.risposta(System.lineSeparator())).given(viewSpy).print(matches(BENVENUTO.pattern()));
		willAnswer(rispostaPerView.risposta("mossa_aleatoria")).given(viewSpy).print(matches(TURNO.pattern()));
		willAnswer(rispostaPerView.risposta("ABORT")).given(viewSpy).print(TURNO_FINITO.pattern());
		
		//when
		viewSpy.run();
		
		//then
		verify(viewSpy).chiediMossa(any(Event.class));
	}

}
