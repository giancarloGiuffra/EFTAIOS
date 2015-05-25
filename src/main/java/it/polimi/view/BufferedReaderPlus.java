package it.polimi.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public abstract class BufferedReaderPlus extends BufferedReader {

	protected BufferedReader bufferedReader;
	
	/**
	 * Costruttore
	 * @param in
	 */
	public BufferedReaderPlus(Reader in) {
		super(in);
		this.bufferedReader = new BufferedReader(in);
	}

	/**
	 * Costruttore
	 * @param in
	 * @param sz
	 */
	public BufferedReaderPlus(Reader in, int sz) {
		super(in, sz);
		this.bufferedReader = new BufferedReader(in);
	}
	
	@Override
	public abstract String readLine() throws IOException;

}
