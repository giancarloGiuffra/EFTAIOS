package it.polimi.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class BufferedReaderPlus {

	protected BufferedReader bufferedReader;
	
	/**
	 * Costruttore vuoto
	 */
	public BufferedReaderPlus(){
		
	}
	
	/**
	 * Costruttore
	 * @param inputStream
	 */
	public BufferedReaderPlus(InputStream inputStream){
		this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
	}
	
	/**
	 * chiama readLine
	 * @return
	 * @throws IOException
	 */
	public String readLine() throws IOException{
		return this.bufferedReader.readLine();
	};
	
	/**
	 * chiude lo stream
	 * @throws IOException
	 */
	public void close() throws IOException{
		this.bufferedReader.close();
	}

}
