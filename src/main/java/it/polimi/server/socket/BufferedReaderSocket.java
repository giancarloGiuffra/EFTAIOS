package it.polimi.server.socket;

import java.io.IOException;
import java.io.Reader;

import it.polimi.view.BufferedReaderPlus;

public class BufferedReaderSocket extends BufferedReaderPlus {

	public BufferedReaderSocket(Reader in) {
		super(in);
		// TODO Auto-generated constructor stub
	}

	public BufferedReaderSocket(Reader in, int sz) {
		super(in, sz);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String readLine() throws IOException {
		return this.bufferedReader.readLine();
	}

}
