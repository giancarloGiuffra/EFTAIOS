package it.polimi.server.socket;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import it.polimi.view.PrintWriterPlus;

public class PrintWriterSocket extends PrintWriterPlus {

	/**
	 * Costruttore
	 * @param out
	 */
	public PrintWriterSocket(OutputStream out) {
		super(out);
	}

	/**
	 * Costruttore
	 * @param out
	 * @param autoFlush
	 */
	public PrintWriterSocket(OutputStream out, boolean autoFlush) {
		super(out, autoFlush);
	}

}
