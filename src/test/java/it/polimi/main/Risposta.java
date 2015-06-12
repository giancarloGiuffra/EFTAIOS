package it.polimi.main;

import java.io.ByteArrayInputStream;

import it.polimi.view.BufferedReaderPlus;
import it.polimi.view.View;

import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class Risposta implements Answer<Void> {
	
	protected View view;
	protected String risposta;
	
	/**
	 * Costruttore
	 * @param view
	 * @param risposta
	 */
	Risposta(View view, String risposta){
		this.view = view;
		this.risposta = risposta;
	}
	
	/**
	 * crea un BufferedReaderPlus contenente la stringa passata
	 * @param string
	 * @return
	 */
	public static BufferedReaderPlus risposta(String string){
		return new BufferedReaderPlus(new ByteArrayInputStream(string.getBytes()));
	}
	
	/**
	 * getter per view
	 * @return
	 */
	public View view(){
		return this.view;
	}
	
	/**
	 * getter per risposta
	 * @return
	 */
	public String risposta(){
		return this.risposta;
	}
	
	
	/**
	 * assegna al inputstream della View un BufferedReader contenente la stringa passata in costruttore
	 */
	@Override
	public Void answer(InvocationOnMock invocation) throws Throwable {
		this.view.setInput(risposta(this.risposta));
		return null;
	}

}
