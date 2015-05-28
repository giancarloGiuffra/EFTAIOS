package it.polimi.server.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import it.polimi.view.BufferedReaderPlus;

public class BufferedReaderSocket extends BufferedReaderPlus {

	/**
	 * Costruttore
	 * @param inputStream
	 */
	public BufferedReaderSocket(InputStream inputStream) {
		super(inputStream);
	}

}
