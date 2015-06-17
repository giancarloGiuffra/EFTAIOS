package it.polimi.server;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

import it.polimi.client.SocketInterface;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class Risposta implements Answer<Void> {
	
	protected SocketInterface socketInterface;
	protected String risposta;
	
	/**
	 * Costruttore
	 * @param view
	 * @param risposta
	 */
	Risposta(SocketInterface socketInterface, String risposta){
		this.socketInterface = socketInterface;
		this.risposta = risposta;
	}
	
	/**
	 * crea un BufferedReaderPlus contenente la stringa passata
	 * @param string
	 * @return
	 */
	public static BufferedReader risposta(String string){
		return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(string.getBytes())));
	}
	
	/**
	 * getter per view
	 * @return
	 */
	public SocketInterface socketInterface(){
		return this.socketInterface;
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
		this.socketInterface.setStdIn(risposta(this.risposta));
		return null;
	}

}
